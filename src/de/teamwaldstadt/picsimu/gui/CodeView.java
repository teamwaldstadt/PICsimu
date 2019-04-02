package de.teamwaldstadt.picsimu.gui;

import java.io.File;

import javax.swing.JTextArea;

public class CodeView extends JTextArea {
	
	private static final long serialVersionUID = 1L;

	public void loadCode(File file) {
		System.out.println(file);
	}
}
