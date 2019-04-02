package de.teamwaldstadt.picsimu.gui.storagetable;

import javax.swing.table.DefaultTableModel;

public class StorageTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int row, int col) {
		return !(row == 0 || col == 0);
	}
}
