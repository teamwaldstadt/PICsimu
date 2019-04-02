package de.teamwaldstadt.picsimu.gui;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeView extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	int currentLine = 0;
	
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
		//setCellSelectionEnabled(false);
		
		tm.setColumnCount(2);
		tm.setRowCount(20);
		setTableHeader(null);
		for (int i = 0; i < 20; i++) {
			tm.setValueAt(i, i, 0);
		}
		
		setModel(tm);
		setRowHeight(16);
		
		getColumnModel().getColumn(0).setMaxWidth(20);
		getColumnModel().getColumn(1).setPreferredWidth(200);
		setFont(new Font("consolas", Font.PLAIN, 14));
		
		cwcr = new CodeViewCellRenderer();
		setDefaultRenderer(Object.class, cwcr);
		setModel(tm);
	}
	
	public void loadCode(File file) {
		currentLine = 0;
		String[] lines = null;
		try {
			lines = Parser.getAllLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tm.setRowCount(lines.length);
		for (int i = 0; i < lines.length; i++) {
			tm.setValueAt(i, i, 0);
			tm.setValueAt(lines[i], i, 1);
		}
		selectLine(0);
		setModel(tm);
	}
	private void selectLine(int lineNr) {
		cwcr.ROW = lineNr;
	}
	public void setLine(int line) {
		currentLine = line;
		selectLine(currentLine);
		if (line < getRowCount() - 5) line += 5;
		changeSelection(line, 0, false, false);
		repaint();
	}
	public void resetLine() {
		currentLine = 0;
		selectLine(currentLine);
		repaint();
	}
}
