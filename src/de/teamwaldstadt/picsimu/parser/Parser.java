package de.teamwaldstadt.picsimu.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.command.CommandConverter;
import de.teamwaldstadt.picsimu.command.CommandSet;

public class Parser {

	public static String[] getAllLines(File file) throws IOException {
		List<String> content = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;

		while ((line = reader.readLine()) != null) {
				content.add(line);
		}
		reader.close();
		
		return content.toArray(new String[0]);
	}
	
	private static String[] loadFile(File file) throws IOException {
		List<String> content = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		
		while ((line = reader.readLine()) != null) {
			line = line.replaceAll("([0-9a-fA-F]{4} [0-9a-fA-F]{4}.*[0-9]{5}).*", "$1");
			
			if (line.matches("[0-9a-fA-F]{4} [0-9a-fA-F]{4}.*[0-9]{5}")) {
				line = line.replaceAll(" {1,}", " ");
				content.add(line);
			}
		}
		reader.close();
		
		return content.toArray(new String[0]);
	}
	
	public static CommandSet[] getCommandList(File file) throws IOException {
		String[] lines = loadFile(file);
		CommandSet[] set = new CommandSet[lines.length];
		
		for (int i = 0; i < lines.length; i++) {
			int commandNr = Integer.parseInt(lines[i].split(" ")[0], 16);
			int command = Integer.parseInt(lines[i].split(" ")[1], 16);
			int lineNr = Integer.parseInt(lines[i].split(" ")[2]);
			
			Command c = CommandConverter.convert(command);
			
			//invert mask and AND with command to get argument
			int argument = command & (c.getMask() ^ 0xFFFF);		
			
			set[i] = new CommandSet(c, argument, commandNr, lineNr);
		}
		return set;
		
	}
	
	
	

}
