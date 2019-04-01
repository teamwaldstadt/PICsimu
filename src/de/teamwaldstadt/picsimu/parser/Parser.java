package de.teamwaldstadt.picsimu.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import de.teamwaldstadt.picsimu.command.AllCommands;
import de.teamwaldstadt.picsimu.command.CommandConverter;

public class Parser {

	public String[] loadFile(String filename) throws IOException {
		List<String> content = new ArrayList<>();
		Scanner sc = new Scanner(new File(filename));

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			line = line.replaceAll("([0-9a-fA-F]{4} [0-9a-fA-F]{4}).*", "$1");
			if (line.matches("[0-9a-fA-F]{4} [0-9a-fA-F]{4}")) {
				content.add(line);
			}
		}
		sc.close();
		
		
		return content.toArray(new String[0]);
	}
	
	public HashMap<Integer, AllCommands>[] getCommandList(String filename) throws IOException {
		String[] lines = loadFile(filename);
				
		for (int i = 0; i < lines.length; i++) {
			//int lineNr = Integer.parseInt(lines[i].split(" ")[0], 16);
			int command = Integer.parseInt(lines[i].split(" ")[1], 16);
			
			AllCommands c = CommandConverter.convert(command);
			System.out.println(c);
			
			//return null;
			//commands[i] = c;
		}
		return null;
		
	}
	
	
	

}
