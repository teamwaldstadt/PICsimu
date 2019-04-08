package de.teamwaldstadt.picsimu;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import de.teamwaldstadt.picsimu.command.CommandExecutor;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIPanel;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeExecutor {
	CommandSet[] commands;
	
	GUIPanel gui;
	
	boolean DONE;
	
	public CodeExecutor() {
		DONE = false;
		GUIWindow w = new GUIWindow(this);
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
		DONE = false;
		commands = null;
		try {
			commands = Parser.getCommandList(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui.getCodeView().setLine(commands[0].getLineNr());
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
		if (commands == null || commands.length == 0 || DONE) {
			return;
		}
		
		//System.out.println("cmdNr: " + commandNr + ", commands: " + commands.length);
		
		try {
			runCommand(Main.STORAGE.getPC());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if (Main.STORAGE.getPC() >= commands.length - 1) {
				// Code executed. User must click 'reset'
				DONE = true;
			} else {
				Main.STORAGE.setPC(Main.STORAGE.getPC() + 1);
				gui.getCodeView().setLine(commands[Main.STORAGE.getPC()].getLineNr());
			}
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
				System.out.println("Command '" + commands[commandNr].getCommand() + "' not yet defined");
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
		
		
		updateStorage();
		updateRegisters();
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
