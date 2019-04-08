package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class JInfoTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	public JInfoTable(CodeExecutor codeExecutor) {
		DefaultTableModel tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {
				if (col == 1 && row == 1) return false;
				return col != 0;
			}
			@Override
			public void setValueAt(Object data, int row, int col) {
				String value = String.valueOf(data);
				if (col == 0) { 
					super.setValueAt(data, row, col); 
					return; 
				}
				
				if (row == 1) {
					super.setValueAt(data, row, col);
					return;
				}
				
				if (!value.matches("[0-9a-fA-F]{1,2}")) {
					JOptionPane.showMessageDialog(null, "Value not allowed!", "Alert", JOptionPane.CANCEL_OPTION); 
				} else {
					if (value.length() == 1) 
						value = "0" + value;
					super.setValueAt(value.toUpperCase(), row, col);
					
					if (row == 0)
						codeExecutor.updateWReg(Integer.parseInt(value, 16));
				}
			}
		};
		int width = 20;
		tm.setColumnCount(2);
		setModel(tm);

		getColumnModel().getColumn(0).setPreferredWidth(width);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		tm.setRowCount(2);
		setTableHeader(null);
		setCellSelectionEnabled(false);
		
		setModel(tm);
		setRowHeight(width);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		setGridColor(Color.GRAY);
		setModel(tm);
		setValueAt("W", 0, 0);
		setValueAt("PC", 1, 0);
		update();
	}
	
	public void update() {
		setValueAt(String.format("%02X", Main.STORAGE.getW()), 0, 1);
		setValueAt("0000", 1, 1);
	}
	
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    	if (row % 2 == 1)
    		setBackground(Color.WHITE);
    	else 
    		setBackground(new Color(240, 240, 240));
		Component c = super.prepareRenderer(renderer, row, column);
        return c;
    }
	@Override
	public boolean editCellAt(int row, int column, EventObject e){
        boolean result = super.editCellAt(row, column, e);
        final Component editor = getEditorComponent();
        if (editor == null || !(editor instanceof JTextComponent)) {
            return result;
        }
        if (e instanceof KeyEvent) {
            ((JTextComponent) editor).selectAll();
        }
        return result;
    }
}