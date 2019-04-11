package de.teamwaldstadt.picsimu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;
import javax.swing.Timer;

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
	public int DELAY_FACTOR = 100; 
	
	CommandSet[] commands;
	
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
	
	public boolean lineHasCode(int line) {
		if (commands == null) return false;
		for (CommandSet c : commands) {
			if (c.getLineNr() == line) {
				return true;
			}
		}
		return false;
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
	}
	
	public void nextCommand() {		
		try {
//			System.out.println("pc: " + Main.STORAGE.getPC() + " commands: " + commands.length);
			
			if (commands == null || commands.length == 0 || DONE) {
				return;
			}
			
			//runCommand(Main.STORAGE.getPC());
			runtime += commandDuration * (commands[correctPC(Main.STORAGE.getPC())].getCommand().getQuartzTacts() / 4);
			
			runCommand(correctPC(Main.STORAGE.getPC()));
			
			if (correctPC(Main.STORAGE.getPC()) >= commands.length) {
				DONE = true;
				JOptionPane.showMessageDialog(null, "Letzter Befehl ausgef\u00FChrt.");
				return;
			}
			
			
			gui.getCodeView().setLine(commands[correctPC(Main.STORAGE.getPC())].getLineNr());
			//gui.getCodeView().setLine(commands[Main.STORAGE.getPC()].getLineNr());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gui.setRuntime(runtime);
		
		updateRegisters();
		updateStorage();
		
		
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
				Main.STORAGE.setPC(Main.STORAGE.getPC() + 1);
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
	@Override
	public void actionPerformed(ActionEvent e) {
		if (commands != null)
			nextCommand();
	}
	public void start() {
		if (!t.isRunning() && commands != null) t.start();
	}
	public void stop() {
		if (t.isRunning()) t.stop();
	}
}
