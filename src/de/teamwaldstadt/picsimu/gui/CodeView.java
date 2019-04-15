package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeView extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	DefaultTableModel tm;
	int selected = 0;
	
	public CodeView() {
		tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		tm.setColumnCount(2);
		tm.setRowCount(20);
		setModel(tm);

		getColumnModel().getColumn(0).setMaxWidth(10);
		getColumnModel().getColumn(1).setPreferredWidth(500);
		setTableHeader(null);
		setCellSelectionEnabled(false);
		setGridColor(Color.GRAY);
		setRowHeight(16);
		setFont(new Font("courier", Font.PLAIN, 14));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//toggle 'B' flag
				if (columnAtPoint(e.getPoint()) > 0) return;
				if (String.valueOf(getValueAt(rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()))).equals("B")) {
					int line = rowAtPoint(e.getPoint());
					int commandNr = -1;
					for (int i = 0; i < Main.EXECUTOR.getCommands().length; i++) {
						if (line == Main.EXECUTOR.getCommands()[i].getLineNr()) {
							commandNr = Main.EXECUTOR.getCommands()[i].getCommandNr();
							break;
						}
					}
					if (commandNr != -1) {
						setValueAt("", rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()));
						Main.EXECUTOR.removeBreakpoint(commandNr);
					} else {
						System.out.println("Breakpoint error");
					}
				} else if (Main.EXECUTOR.getCommandSetAt(rowAtPoint(e.getPoint())) != null) {
					int line = rowAtPoint(e.getPoint());
					int commandNr = -1;
					for (int i = 0; i < Main.EXECUTOR.getCommands().length; i++) {
						if (line == Main.EXECUTOR.getCommands()[i].getLineNr()) {
							commandNr = Main.EXECUTOR.getCommands()[i].getCommandNr();
							break;
						}
					}
					if (commandNr != -1) {
						setValueAt("B", rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()));
						Main.EXECUTOR.setBreakpoint(commandNr);
					} else {
						System.out.println("Breakpoint error");
					}
				}
			}
		});
	}
	
	public void removeBreakPoints() {
		for (int i = 0; i < getRowCount(); i++) {
			setValueAt("", i, 0);
		}
	}
	
	public void loadCode(File file) {
		String[] lines = null;
		try {
			lines = Parser.getAllLines(file);
			tm.setRowCount(lines.length);
			for (int i = 0; i < lines.length; i++) {
				tm.setValueAt(lines[i], i, 1);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.CANCEL_OPTION);
		}
	}
	public void setLine(int line) {
		changeSelection(0, 0, false, false);
		this.selected = line;
		if (line < getRowCount() - 8) 
			line += 8;
		else
			line = getRowCount();
		changeSelection(line, 0, false, false);
		repaint();
	}
	
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
   
		if (row % 2 == 1)
    		setBackground(Color.WHITE);
    	else 
    		setBackground(new Color(240, 240, 240));
		
		if (row == selected)
        	setBackground(Color.YELLOW);
		Component c = super.prepareRenderer(renderer, row, column);

		Object value = getValueAt(row, column);
		if (value != null && c instanceof JLabel) {
        	JLabel l = (JLabel) c;
        	String tooltip = null;
        	if (String.valueOf(value).contains(";")) 
        		tooltip = String.valueOf(value).replaceAll(".*;", "");
        	l.setToolTipText(tooltip);
        	
        }        
		return c;
    }
}
