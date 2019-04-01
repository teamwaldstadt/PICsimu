package de.teamwaldstadt.picsimu.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

	public String[] loadFile(String filename) throws IOException {
		List<String> content = new ArrayList<>();
		Scanner sc = new Scanner(new File(filename));

		while (sc.hasNextLine()) {
			content.add(sc.nextLine());
		}

		sc.close();
		return content.toArray(new String[0]);
	}

}
