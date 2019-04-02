package de.teamwaldstadt.picsimu.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeView extends JTable {
	
	private static final long serialVersionUID = 1L;

	DefaultTableModel tm;
	public CodeView() {
		tm = new DefaultTableModel();
		tm.setColumnCount(2);
		tm.setRowCount(20);
		setTableHeader(null);
		for (int i = 0; i < 20; i++) {
			tm.setValueAt(i, i, 0);
		}
		setModel(tm);
		setRowHeight(16);
		getColumnModel().getColumn(0).setMaxWidth(20);
		getColumnModel().getColumn(1).setMinWidth(200);
		setModel(tm);
	}
	
	public void loadCode(File file) {
		String[] lines = null;
		try {
			lines = Parser.getAllLines(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tm.setRowCount(lines.length);
		for (int i = 0; i < lines.length; i++) {
			tm.setValueAt(i, i, 0);
			tm.setValueAt(lines[i], i, 1);
		}
		
		setModel(tm);
	}
}
