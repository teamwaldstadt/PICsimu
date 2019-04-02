package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class StorageTableCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (row == 0 || column == 0)
        	setBackground(Color.LIGHT_GRAY);
        else 
        	setBackground(Color.WHITE);
        return c;
    }
}
