package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StorageTableModel extends DefaultTableModel {
	@Override
	public boolean isCellEditable(int row, int col) {
		return !(row == 0 || col == 0);
	}
}
