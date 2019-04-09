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
import de.teamwaldstadt.picsimu.Main;

public class StorageTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	StorageTableModel tm;
	
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
		
		for (int i = 1; i < 9; i++)
			tm.setValueAt(String.format("%02X", (i-1)), 0, i);
		for (int i = 0; i < 33; i++)
			tm.setValueAt(String.format("%02X", i * 8 - 8), i, 0);	
		tm.setValueAt("", 0, 0);
		
		update();
	}
	
	public void update() {
		for (int i = 1; i < 33; i++) {
			for (int j = 1; j < 9; j++) {
				//if (i == 1) System.out.print(Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)] + " ");
				tm.setValueJustForGUI(String.format("%02X", Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)]), i, j);
			}
			//System.out.println();
		}
	}
	
	public StorageTableModel getStorageTableModel() {
		return tm;
	}
	
	public void updateWithoutRegisterUpdate() {
		for (int i = 1; i < 33; i++) {
			for (int j = 1; j < 9; j++) {
				//if (i == 1) System.out.print(Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)] + " ");
				tm.setHardValueAt(String.format("%02X", Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)]), i, j);
			}
			//System.out.println();
		}
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
        if (row == 0 || column == 0)
        	setBackground(Color.LIGHT_GRAY);
        else 
        	if (row % 2 == 1)
        		setBackground(Color.WHITE);
        	else 
        		setBackground(new Color(240, 240, 240));
		Component c = super.prepareRenderer(renderer, row, column);
        return c;
    }
}
