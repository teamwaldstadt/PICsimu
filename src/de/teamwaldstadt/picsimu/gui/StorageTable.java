package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import de.teamwaldstadt.picsimu.CodeExecutor;

public class StorageTable extends JTable {

	private static final long serialVersionUID = 1L;
	StorageTableModel tm;
	int j = 0;
	public StorageTable(CodeExecutor codeExecutor) {
		tm = new StorageTableModel(codeExecutor);
		int width = 20;
		
		tm.setColumnCount(9);
		tm.setRowCount(33);
		
		setRowHeight(width + 5);
		setCellSelectionEnabled(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setTableHeader(null);
		setGridColor(Color.GRAY);
		setModel(tm);
		
		getColumnModel().getColumn(0).setPreferredWidth(width);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		getColumnModel().getColumn(2).setPreferredWidth(width);
		getColumnModel().getColumn(3).setPreferredWidth(width);
		getColumnModel().getColumn(4).setPreferredWidth(width);
		getColumnModel().getColumn(5).setPreferredWidth(width);
		getColumnModel().getColumn(6).setPreferredWidth(width);
		getColumnModel().getColumn(7).setPreferredWidth(width);
		getColumnModel().getColumn(8).setPreferredWidth(width);
		
		
		tm.init();
	
		updateGUI();
	}
	
	public void updateGUI() {
		tm.updateGUI();
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
	
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {		
        if (row == 0 || column == 0) {
        	setBackground(Color.LIGHT_GRAY);
        } else {
    		setToolTipText("Adresse: 0x" + String.format("%2X", (column-1) + (getColumnCount() - 1) * (row-1)).replaceAll(" ", "0"));
//        	setToolTipText("row: " + row + " col: " + column);
        	
        	if (row % 2 == 1)
        		setBackground(Color.WHITE);
        	else 
        		setBackground(new Color(240, 240, 240));
        }
        
		Component c = super.prepareRenderer(renderer, row, column);
        return c;
    }
}
