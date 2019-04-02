package de.teamwaldstadt.picsimu.gui.storagetable;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import de.teamwaldstadt.picsimu.Main;

public class StorageTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	StorageTableModel tm;
	
	public StorageTable() {
		
		tm = new StorageTableModel();
		int width = 20;
		
		tm.setColumnCount(9);
		tm.setRowCount(33);
		
		

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
		
		setDefaultRenderer(Object.class, new StorageTableCellRenderer());
		
		
		getColumnModel().getColumn(0).setPreferredWidth(width);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		getColumnModel().getColumn(2).setPreferredWidth(width);
		getColumnModel().getColumn(3).setPreferredWidth(width);
		getColumnModel().getColumn(4).setPreferredWidth(width);
		getColumnModel().getColumn(5).setPreferredWidth(width);
		getColumnModel().getColumn(6).setPreferredWidth(width);
		getColumnModel().getColumn(7).setPreferredWidth(width);
		getColumnModel().getColumn(8).setPreferredWidth(width);
		
		for (int i = 1; i < 9; i++)
			tm.setValueAt(String.format("%02X", (i-1)), 0, i);
		for (int i = 0; i < 33; i++)
			tm.setValueAt(String.format("%02X", i * 8 - 8), i, 0);	
		tm.setValueAt("", 0, 0);
		
		update();
	}
	
	public void update() {
		for (int i = 1; i < 9; i++) {
			for (int j = 1; j < 33; j++) {
				tm.setValueAt(String.format("%02X", Main.STORAGE.getStorage()[(i-1) + 8 * (j-1)]), j, i);
			}
		}
		setModel(tm);
	}
}
