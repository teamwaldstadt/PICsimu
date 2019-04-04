package de.teamwaldstadt.picsimu.gui;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeView extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	CodeViewCellRenderer cwcr;
	DefaultTableModel tm;
	public CodeView() {
		tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		tm.setColumnCount(1);
		tm.setRowCount(20);
		setTableHeader(null);
		
		setModel(tm);
		setRowHeight(16);
		setFont(new Font("courier", Font.PLAIN, 14));
		
		cwcr = new CodeViewCellRenderer();
		setDefaultRenderer(Object.class, cwcr);
		setModel(tm);
	}
	
	public void loadCode(File file) {
		String[] lines = null;
		try {
			lines = Parser.getAllLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tm.setRowCount(lines.length);
		for (int i = 0; i < lines.length; i++) {
			tm.setValueAt(lines[i], i, 0);
		}
		setModel(tm);
	}
	private void selectLine(int lineNr) {
		cwcr.ROW = lineNr;
	}
	public void setLine(int line) {
		selectLine(line);
		if (line < getRowCount() - 5) line += 5;
		changeSelection(line, 0, false, false);
		repaint();
	}
}
