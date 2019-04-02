package de.teamwaldstadt.picsimu.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.teamwaldstadt.picsimu.command.Command;
import de.teamwaldstadt.picsimu.command.CommandConverter;
import de.teamwaldstadt.picsimu.command.CommandSet;

public class Parser {

	public static String[] getAllLines(String filename) throws IOException {
		List<String> content = new ArrayList<>();
		Scanner sc = new Scanner(new File(filename));

		while (sc.hasNextLine()) {
				content.add(sc.nextLine());
		}
		sc.close();
		
		return content.toArray(new String[0]);
	}
	
	public String[] loadFile(String filename) throws IOException {
		List<String> content = new ArrayList<>();
		Scanner sc = new Scanner(new File(filename));

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			line = line.replaceAll("([0-9a-fA-F]{4} [0-9a-fA-F]{4}.*[0-9]{4}).*", "$1");
			
			if (line.matches("[0-9a-fA-F]{4} [0-9a-fA-F]{4}.*[0-9]{4}")) {
				line = line.replaceAll(" {1,}", " ");
				content.add(line);
			}
		}
		sc.close();
		
		
		return content.toArray(new String[0]);
	}
	
	public CommandSet[] getCommandList(String filename) throws IOException {
		String[] lines = loadFile(filename);
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
