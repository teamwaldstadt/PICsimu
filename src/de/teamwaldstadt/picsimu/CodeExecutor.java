package de.teamwaldstadt.picsimu;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;

import de.teamwaldstadt.picsimu.command.CommandExecutor;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.GUIPanel;
import de.teamwaldstadt.picsimu.gui.GUIWindow;
import de.teamwaldstadt.picsimu.parser.Parser;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class CodeExecutor {
	int commandNr = 0;
	CommandSet[] commands;
	
	GUIPanel gui;
	
	public CodeExecutor() {
		GUIWindow w = new GUIWindow(this);
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
		commands = null;
		try {
			commands = Parser.getCommandList(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gui.getCodeView().setLine(commands[0].getLineNr());
	}
	
	public void reset() {
		gui.getCodeView().setLine(commands[0].getLineNr());
		Main.STORAGE.resetAll();
		gui.getStorageTable().update();
	}
	
	public void nextCommand() {
		if (commands == null || commands.length == 0)
			return;
		
		runCommand(commandNr);
		
		commandNr++;
		
		if (commandNr >= commands.length) 
			commandNr = 0;
		
		gui.getCodeView().setLine(commands[commandNr].getLineNr());
	}
	
	public void runCommand(int commandNr) {
		try {
			Constructor<?> commandConstructor = commands[commandNr].getCommand().getExecutor().getConstructors()[0];
			if (commandConstructor.getParameterTypes().length == 0) {
				System.out.println("Command '" + commands[commandNr].getCommand() + "' not yet defined");
			} else {
				System.out.println("Command '" + commands[commandNr].getCommand() + "' executed successfully");
				CommandExecutor c = (CommandExecutor) commandConstructor.newInstance(commands[commandNr].getArgument());
				c.execute();
				
			}
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem with execute()");
			e.printStackTrace();
		}
		System.out.println("W-Reg: " + String.format("%02X", Main.STORAGE.getW()) + "h");
		System.out.println("Z-Flag: " + ((Main.STORAGE.getRegister(SpecialRegister.STATUS) & 0x04) >> 2));
		gui.getStorageTable().update();
	}
	
}
