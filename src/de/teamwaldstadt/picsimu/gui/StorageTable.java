package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class StorageTable extends JTable {
	public StorageTable() {
		
		StorageTableModel tm = new StorageTableModel();
		int width = 20;
		
		tm.setColumnCount(8);
		tm.setRowCount(32);
		
		

		tm.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getFirstRow() == 0 && e.getColumn() == 0) return;
				if (!tm.getValueAt(e.getFirstRow(), e.getColumn()).toString().matches("[0-9a-fA-F]{2}")) {
					JOptionPane.showMessageDialog(null, "Value not allowed!", "Alert", JOptionPane.CANCEL_OPTION);
					tm.setValueAt("00", e.getFirstRow(), e.getColumn());
				}
			}
		});
		
		setRowHeight(width);
		setTableHeader(null);
		setModel(tm);
		
		//getColumnModel().getColumn(0).setCellRenderer(new StorageTableCellRenderer());
		//getColumnModel().getColumn(1).setCellRenderer(new StorageTableCellRenderer());
		setDefaultRenderer(Object.class, new StorageTableCellRenderer());
		
		
		getColumnModel().getColumn(0).setPreferredWidth(width);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		getColumnModel().getColumn(2).setPreferredWidth(width);
		getColumnModel().getColumn(3).setPreferredWidth(width);
		getColumnModel().getColumn(4).setPreferredWidth(width);
		getColumnModel().getColumn(5).setPreferredWidth(width);
		getColumnModel().getColumn(6).setPreferredWidth(width);
		getColumnModel().getColumn(7).setPreferredWidth(width);
		
		for (int i = 0; i < 8; i++)
			tm.setValueAt(String.format("%02X", i), 0, i);
		for (int i = 0; i < 32; i++)
			tm.setValueAt(String.format("%02X", i * 8), i, 0);	
		tm.setValueAt("", 0, 0);
		for (int i = 1; i < 8; i++) {
			for (int j = 1; j < 32; j++) {
				tm.setValueAt("00", j, i);
			}
		}
		
		setModel(tm);
	}
}
