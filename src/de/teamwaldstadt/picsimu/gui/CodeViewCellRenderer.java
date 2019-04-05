package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CodeViewCellRenderer extends DefaultTableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	int ROW = 0;
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null && c instanceof JLabel) {
        	JLabel l = (JLabel) c;
        	String tooltip = "";
        	if (String.valueOf(value).contains(";"))
        		tooltip = String.valueOf(value).replaceAll(".*;", "");
        	
        	l.setToolTipText(tooltip);
        }
        if (row == this.ROW)
        	setBackground(Color.YELLOW);
        else 
        	setBackground(Color.WHITE);
        return c;
    }
}
