package de.teamwaldstadt.picsimu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.command.CommandExecutor;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIPanel;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;
import de.teamwaldstadt.picsimu.utils.FrequencyGenerator;

public class CodeExecutor implements ActionListener {
	
	/*
	 * factor, that gets multiplied with the real delay in micro seconds, to create the delay in ms
	 * example: real delay: 1us -> simulated delay: 1us * 10 -> 10ms
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
		t = new Timer(100, this);
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
	}
	
	public void nextCommand() {
		if (commands[Main.STORAGE.getPC()].hasBreakpoint()) {
			stop();
			return;
		}
		
		try {
			if (commands == null || commands.length == 0 || DONE) {
				return;
			}
			
			this.incrementRuntime(commands[correctPC(Main.STORAGE.getPC())].getCommand());
			
			runCommand(correctPC(Main.STORAGE.getPC()));
			
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
		t.setDelay((int) (commandDuration * DELAY_FACTOR)); 
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

		if (commands != null)
			nextCommand();
	}
	
	public void start() {
		if (!t.isRunning() && commands != null && !commands[Main.STORAGE.getPC()].hasBreakpoint()) t.start();
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
