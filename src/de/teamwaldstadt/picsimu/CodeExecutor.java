package de.teamwaldstadt.picsimu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.command.CommandExecutor;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIPanel;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.utils.FrequencyGenerator;
import de.teamwaldstadt.picsimu.utils.Utils;

public class CodeExecutor implements ActionListener {
	
	boolean WATCHDOG_ENABLED = true;
	
	
	/*
	 * factor, that gets multiplied with the real delay in micro seconds, to create the delay in ms
	 * example: real delay: 1us -> simulated delay: 1us * DELAY_FACTOR -> x ms
	 * 
	 * if this FACTOR is 1, then 1kHz = 1s delay
	 * with 100, 100kHz = 1s delay, or 1kHz = 100s delay
	 */
	public int DELAY_FACTOR = 50; 
	
	CommandSet[] commands;
	boolean[] breakpoints;
	
	public GUIPanel gui;
	
	boolean DONE;
	GUIWindow w;
	Timer t;
	
	// values in micro seconds
	double runtime;
	double commandDuration = 0;
	
	public CodeExecutor() {
		runtime = 0;
		t = new Timer(1, this);
		DONE = false;
		w = new GUIWindow(this);
		Main.STORAGE.resetAll();
		gui = w.getPanel();
		FrequencyGenerator.getInstance().setCodeExecutor(this);
	}
	
	public CommandSet getCommandSetAt(int line) {
		if (commands == null) return null;
		
		for (CommandSet c : commands) {
			if (c.getLineNr() == line) {
				return c;
			}
		}
		
		return null;
	}
	
	public void loadFile(File file) {
		gui.getCodeView().loadCode(file);
		w.setTitle(Main.PGM_NAME + " " + Main.PGM_VERSION + " - " + file.getAbsolutePath());
		DONE = false;
		commands = null;
		try {
			commands = Parser.getCommandList(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		breakpoints = new boolean[commands.length];
		reset();
	}
	
	public void reset() {
		if (commands != null && commands.length > 0) {
			gui.getCodeView().setLine(commands[0].getLineNr());
		}
		stop();
		DONE = false;
		Main.STORAGE.resetAll();
		updateStorage();
		updateRegisters();
		runtime = 0;
		gui.setRuntime(runtime);
		if (breakpoints != null) {
			for (int i = 0; i < breakpoints.length; i++) {
				breakpoints[i] = false;
			}
		}
		gui.getCodeView().removeBreakPoints();
		watchdogCounter = 0;
		awaitInterrupt = false;
		watchdogInterrupt = false;
	}
	
	int prescaler = 4;
	public static int prescalerTact = 0;
	
	boolean awaitInterrupt = false;
	boolean watchdogInterrupt = false;
	
	public void nextCommand() {
		try {
			//if there is no command available: return
			if (commands == null || commands.length == 0 || DONE) {
				return;
			}
			
			boolean gie = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 7, true);
			boolean toie = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 5, true);
			boolean inte = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 4, true);
			boolean rbie = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 3, true);
			boolean toif = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 2, true);
			boolean intf = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 1, true);
			boolean rbif = Main.STORAGE.isBitOfRegisterSet(SpecialRegister.INTCON.getAddress(), 0, true);
			
			
			
			boolean timerInterrupt = gie && toie && toif;
			boolean portB0Interrupt = gie && inte && intf;
			boolean portB4to7Interrupt = gie && rbie && rbif;
			
			//detect interrupts
			if (timerInterrupt || portB0Interrupt || portB4to7Interrupt || watchdogInterrupt) {
				awaitInterrupt = false;
				watchdogInterrupt = false;
				
				Main.STORAGE.setBitOfRegister(SpecialRegister.INTCON.getAddress(), 7, false, true);
				
				Main.STORAGE.getStack().push(correctPC(Main.STORAGE.getPC()));
				
				Main.STORAGE.jumpPC(4);
				
				gui.getCodeView().setLine(commands[correctPC(Main.STORAGE.getPC())].getLineNr());
				
				//JOptionPane.showMessageDialog(null, "Int", "titel", JOptionPane.CANCEL_OPTION);
				gui.setRuntime(runtime);
				
				updateRegisters();
				updateStorage();
				
				return;
			}
			
			if (awaitInterrupt) {
				System.out.println("waiting...");
				this.incrementRuntime(commands[correctPC(Main.STORAGE.getPC())].getCommand());

				gui.setRuntime(runtime);
				
				updateRegisters();
				updateStorage();
				
				triggerWatchdog(1);
				return;
			}
			
			this.incrementRuntime(commands[correctPC(Main.STORAGE.getPC())].getCommand());
			
						
			int option = Main.STORAGE.getRegister(SpecialRegister.OPTION_REG.getAddress(), true);
			
			// if TOCS is unset -> timer mode
			if ((option & 32) == 0) {
				triggerTMR0();
				if (DONE) return;
			}
			
			if (commands[correctPC(Main.STORAGE.getPC())].getCommand() == Command.SLEEP) {
				//sleep did not increment the pc
				//now waiting for an interrupt or watchdog
				awaitInterrupt = true;
			}
			
			runCommand(correctPC(Main.STORAGE.getPC()));
			
			if (awaitInterrupt) nextCommand();
			
			if (correctPC(Main.STORAGE.getPC()) >= commands.length) {
				DONE = true;
				JOptionPane.showMessageDialog(null, "Letzter Befehl ausgef\u00FChrt.");
				return;
			}
			
			gui.getCodeView().setLine(commands[correctPC(Main.STORAGE.getPC())].getLineNr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gui.setRuntime(runtime);
		
		updateRegisters();
		updateStorage();
	}
	
	public static int watchdogCounter = 0;
	int watchdogLimit = 18000;
	
	/*
	 * maybe TODO: nicer version of this function
	 */
	public void triggerTMR0() {
		int optionReg = 0;
		try {
			optionReg = Main.STORAGE.getRegister(SpecialRegister.OPTION_REG.getAddress(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (commands == null) return;
		
		//if PSA is set -> use prescaler
		if ((optionReg & 8) == 0) {
			prescaler = (int) Math.pow(2, (optionReg & 7) + 1);
		} else {
			prescaler = 1;
			prescalerTact = 1;
		}
		try {
			int val = Main.STORAGE.getRegister(SpecialRegister.TMR0.getAddress(), true);
			int inc = 0;
			
			int tacts = commands[correctPC(Main.STORAGE.getPC())].getCommand().getQuartzTacts() / 4;
			
			CommandSet c = commands[correctPC(Main.STORAGE.getPC())];
			Command cur = c.getCommand();
			if (cur == Command.BTFSS || cur == Command.BTFSC) {
				int bitIndex = Utils.extractBitsFromIntNumber(c.getArgument(), 7, 3);
				int fileRegister = Utils.extractBitsFromIntNumber(c.getArgument(), 0, 7);
				boolean testBit = Main.STORAGE.isBitOfRegisterSet(fileRegister, bitIndex, false);
				if (cur == Command.BTFSS) {	
					tacts = testBit ? 2 : 1;
				} else if (cur == Command.BTFSC) {
					tacts = testBit ? 1 : 2;
				}
			}
			if (cur == Command.DECFSZ || cur == Command.INCFSZ) {
				int fileRegister = Utils.extractBitsFromIntNumber(c.getArgument(), 0, 7);
				int f = Main.STORAGE.getRegister(fileRegister, false);
				if (cur == Command.DECFSZ) {
					tacts = (f - 1) == 0 ? 2 : 1;
				} else if (cur == Command.INCFSZ) {
					tacts = ((f + 1) & 0xFF) == 0 ? 2 : 1;
				}
			}
			
			
			triggerWatchdog(tacts);
			
			
			if ((optionReg & 32) != 0) tacts = 1;
			
			for (int i = 0; i < tacts; i++) {
				//val++;
				
				if (prescalerTact == prescaler) {
					inc++;
					
					if ((val + inc) % 256 == 0) {
						Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 2, true, true);
						
						//set T0IF
						Main.STORAGE.setBitOfRegister(SpecialRegister.INTCON.getAddress(), 2, true, true);
					} 
					
					prescalerTact = 0;
				} 
				prescalerTact++;
			}
			val = Main.STORAGE.getRegister(SpecialRegister.TMR0.getAddress(), true);
			
			Main.STORAGE.setTimer((val + inc) % 256);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void triggerWatchdog(int tacts) {
		/* watchdog stuff */
		try {
			if (!Main.STORAGE.isBitOfRegisterSet(SpecialRegister.OPTION_REG.getAddress(), 3, true)) {
				watchdogLimit = 18000;
			} else {
				watchdogLimit = 18000 * (int) (Math.pow(2, Utils.extractBitsFromIntNumber(Main.STORAGE.getRegister(SpecialRegister.OPTION_REG.getAddress(), true), 0, 3)));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (int i = 0; i < tacts; i++) {
			watchdogCounter += commandDuration;
			System.out.println(watchdogCounter + " / " + watchdogLimit);
			
			/*
			 * watchdog timer reset
			 */
			if (watchdogCounter > watchdogLimit) {
				
				if (commands[this.correctPC(Main.STORAGE.getPC())].getCommand() != Command.SLEEP) {
					stop();
					DONE = true;
					
					gui.getCodeView().setLine(commands[0].getLineNr());
					
					try {
						Main.STORAGE.jumpPC(0);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						Main.STORAGE.incrementPC();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 3, true, true);
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 4, false, true);
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 5, false, true);
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 6, false, true);
				Main.STORAGE.setBitOfRegister(SpecialRegister.STATUS.getAddress(), 7, false, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				watchdogInterrupt = true;
				this.updateStorage();
				this.updateRegisters();
				return;
			}
		}
		
	}
	
	public void incrementRuntime(Command command) {
		runtime += commandDuration * (command.getQuartzTacts() / 4);
	}
	
	public int correctPC(int pc) {
		if (pc >= commands.length || commands[pc].getCommandNr() != pc) {
			for (int i = 0; i < commands.length; i++) {
				if (commands[i].getCommandNr() == pc) {
					return i;
				}
			}
		}
		return pc;
	}
	
	public void runCommand(int commandNr) throws Exception {
		try {
			Constructor<?> commandConstructor = commands[commandNr].getCommand().getExecutor().getConstructors()[0];
			if (commandConstructor.getParameterTypes().length == 0) {
				System.out.println("Command '" + commands[commandNr].getCommand() + "' not yet defined, skipping");
				Main.STORAGE.incrementPC();
			} else {
				// Expected cause of issue https://github.com/teamwaldstadt/PICsimu/issues/1
				CommandExecutor c = (CommandExecutor) commandConstructor.newInstance(commands[commandNr].getArgument());
				c.execute();
				System.out.println("Command '" + commands[commandNr].getCommand() + "' executed successfully");
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem with execute()");
			e.printStackTrace();
		}
		
//		System.out.println("W-Reg: " + String.format("%02X", Main.STORAGE.getW()) + "h");
//		System.out.println("Carry: " + (Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS, Status.C.getBitIndex()) ? 1 : 0));
//		System.out.println("Digit Carry: " + (Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS, Status.DC.getBitIndex()) ? 1 : 0));
//		System.out.println("Z-Flag: " + (Main.STORAGE.isBitOfRegisterSet(SpecialRegister.STATUS, Status.Z.getBitIndex()) ? 1 : 0));
	}
	
	public void updateStorage() {
		if (gui != null) gui.getStorageTable().updateGUI();
	}
	
	public void updateRegisters() {
		if (gui != null) gui.updateRegistersView();
	}
	
	public void updateWReg(int wreg) {
		try {
			Main.STORAGE.setW(wreg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setQuarzFrequency(double freq) {
		commandDuration = (1 / freq) * 4;
		if (gui != null)
			gui.setCommandDuration(commandDuration);
//		System.out.println((int) (commandDuration * 10));
//		t.setDelay((int) (commandDuration * DELAY_FACTOR));
		t.setDelay(1);
	}

	int stoppedOnLine = -1;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (breakpoints[correctPC(Main.STORAGE.getPC())] && stoppedOnLine != correctPC(Main.STORAGE.getPC())) {
			t.stop();
			stoppedOnLine = correctPC(Main.STORAGE.getPC());
			return;
		}
		stoppedOnLine = -1;

		if (commands != null) {
			nextCommand();
		}
	}
	
	public void start() {
		if (!t.isRunning() && commands != null) t.start();
	}
	
	public void stop() {
		if (t.isRunning()) t.stop();
	}

	public CommandSet[] getCommands() {
		return commands;
	}
	public void setBreakpoint(int commandNr) {
		breakpoints[commandNr] = true;
	}
	public void removeBreakpoint(int commandNr) {
		breakpoints[commandNr] = false;
	}
  
}
