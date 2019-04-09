package de.teamwaldstadt.picsimu;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.swing.JOptionPane;

import de.teamwaldstadt.picsimu.command.CommandExecutor;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIPanel;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeExecutor {
	CommandSet[] commands;
	
	public GUIPanel gui;
	
	boolean DONE;
	GUIWindow w;
	
	public CodeExecutor() {
		DONE = false;
		w = new GUIWindow(this);
		Main.STORAGE.resetAll();
		gui = w.getPanel();
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
	
		DONE = false;
		Main.STORAGE.resetAll();
		updateStorage();
		updateRegisters();
	}
	
	public void nextCommand() {		
		try {
//			System.out.println("pc: " + Main.STORAGE.getPC() + " commands: " + commands.length);
			
			if (commands == null || commands.length == 0 || DONE) {
				return;
			}
			
			runCommand(Main.STORAGE.getPC());
			
			if (Main.STORAGE.getPC() >= commands.length) {
				DONE = true;
				JOptionPane.showMessageDialog(null, "Letzter Befehl ausgeführt.");
				return;
			}
			
			gui.getCodeView().setLine(commands[Main.STORAGE.getPC()].getLineNr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		updateRegisters();
		updateStorage();
		
		
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
		if (gui != null) gui.getStorageTable().update();
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
}
