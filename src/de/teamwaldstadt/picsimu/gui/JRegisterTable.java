package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class JRegisterTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	
	CodeExecutor codeExecutor;
	SpecialRegister reg;
	DefaultTableModel tm;
	public JRegisterTable(String name, CodeExecutor codeExecutor, SpecialRegister reg) {
		this.codeExecutor = codeExecutor;
		this.reg = reg;
		init(name);
		if (reg == SpecialRegister.STATUS) {
			String[] values = {"IRP", "RP1", "RP0", "TO", "PD", "Z", "DC", "C"};
			for (int i = 8; i > 0; i--) {
				setValueAt(values[8 - i], 0, 8 - i + 1);
			}
			tm.setRowCount(2);
		} else {
			for (int i = 8; i > 0; i--) {
				setValueAt(i-1, 0, 8 - i + 1);
			}
		}
		update();
	}
	
	public void update() {
		int val = Main.STORAGE.getStorage()[reg.getAddress()];
		for (int i = 8; i > 0; i--) {
			setValueAt(val & 0x01, getRowCount() - 1, i);
			val = val >> 1;
		}
	}
	
	public void init(String name) {
		tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		int width = 20;
		tm.setColumnCount(9);
		setModel(tm);

		getColumnModel().getColumn(0).setPreferredWidth(width * 2);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		getColumnModel().getColumn(2).setPreferredWidth(width);
		getColumnModel().getColumn(3).setPreferredWidth(width);
		getColumnModel().getColumn(4).setPreferredWidth(width);
		getColumnModel().getColumn(5).setPreferredWidth(width);
		getColumnModel().getColumn(6).setPreferredWidth(width);
		getColumnModel().getColumn(7).setPreferredWidth(width);
		getColumnModel().getColumn(8).setPreferredWidth(width);
		
		tm.setRowCount(3);
		
		setTableHeader(null);
		setCellSelectionEnabled(false);
		
		setFont(new Font("default", Font.PLAIN, 11));
		
		setModel(tm);
		setRowHeight(width);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int col = columnAtPoint(e.getPoint());
				if (row == 0 || col == 0) return;
				if (row != getRowCount() - 1) return;
				
				setValueAt(Integer.parseInt(String.valueOf(getValueAt(row, col))) ^ 1, row, col);
				
				int value = 0;
				for (int i = 0; i < 8; i++) {
					value = (value << 1) + Integer.parseInt(String.valueOf(getValueAt(row, i + 1)));
				}
				
				Main.STORAGE.getStorage()[reg.getAddress()] = value;
				codeExecutor.updateStorage();
			}
		});
		setGridColor(Color.GRAY);
		setModel(tm);
		setValueAt(name, 0, 0);
		setValueAt("Tris", 1, 0);
		setValueAt("Pin", 2, 0);
	}

	
	
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
   
		if (row % 2 == 1)
    		setBackground(Color.WHITE);
    	else 
    		setBackground(new Color(240, 240, 240));
		
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
