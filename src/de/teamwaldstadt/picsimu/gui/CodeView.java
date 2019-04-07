package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.parser.Parser;

public class CodeView extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	DefaultTableModel tm;
	int selected = 0;
	
	public CodeView(CodeExecutor codeExecutor) {
		tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		tm.setColumnCount(2);
		setModel(tm);

		getColumnModel().getColumn(0).setMaxWidth(10);
		getColumnModel().getColumn(1).setPreferredWidth(500);
		tm.setRowCount(20);
		setTableHeader(null);
		setCellSelectionEnabled(false);
		
		setModel(tm);
		setRowHeight(16);
		setFont(new Font("courier", Font.PLAIN, 14));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (columnAtPoint(e.getPoint()) > 0) return;
				if (String.valueOf(getValueAt(rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()))).equals("B")) {
					setValueAt("", rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()));
				} else if (codeExecutor.lineHasCode(rowAtPoint(e.getPoint()))) {
					setValueAt("B", rowAtPoint(e.getPoint()), columnAtPoint(e.getPoint()));
				}
			}
		});
		setGridColor(Color.GRAY);
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
			tm.setValueAt(lines[i], i, 1);
		}
		setModel(tm);
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
