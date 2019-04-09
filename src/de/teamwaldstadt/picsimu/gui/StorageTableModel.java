package de.teamwaldstadt.picsimu.gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class StorageTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	CodeExecutor codeExecutor;
	boolean doUpdate;
	boolean doStorageUpdate;
	
	public StorageTableModel(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		doUpdate = true;
		doStorageUpdate = true;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return !(row == 0 || col == 0);
	}
	
	@Override
	public void setValueAt(Object data, int row, int col) {
		String value = String.valueOf(data);
		if (row == 0 && col == 0) return;
		if (row == 0 || col == 0) { 
			super.setValueAt(data, row, col); 
			return; 
		}
		if (row == 1 && col == 1 && doUpdate) {
			JOptionPane.showMessageDialog(null, "Not a physical register", "Error", JOptionPane.CANCEL_OPTION);
			return;
		}
		
		if (!value.matches("[0-9a-fA-F]{1,2}")) {
			JOptionPane.showMessageDialog(null, "Value not allowed!", "Error", JOptionPane.CANCEL_OPTION); 
		} else {
			if (value.length() == 1) 
				value = "0" + value;
			
			try {
				//System.out.println(value + " at address " + String.format("%02X", ((col-1) + (getColumnCount() - 1) * (row-1))));
				if (doStorageUpdate) {
					Main.STORAGE.setRegister((col-1) + (getColumnCount() - 1) * (row-1), Integer.parseInt(value, 16));
					updateGUIOnly();
				}
				super.setValueAt(value.toUpperCase(), row, col);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.CANCEL_OPTION);
			}
			if (doUpdate) codeExecutor.updateRegisters();
			
		}
	}
	
	public void updateGUIOnly() {
		for (int i = 1; i < 33; i++) {
			for (int j = 1; j < 9; j++) {
				//if (i == 1) System.out.print(Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)] + " ");
				setValueJustForGUI(String.format("%02X", Main.STORAGE.getStorage()[(j-1) + (getColumnCount() - 1) * (i-1)]), i, j);
			}
			//System.out.println();
		}
	}
	
	public void setValueJustForGUI(Object data, int row, int col) {
		doStorageUpdate = false;
		doUpdate = false;
		setValueAt(data, row, col);
		doUpdate = true;
		doStorageUpdate = true;
	}
	
	public void setHardValueAt(Object data, int row, int col) {
		/*int row = 1 + (int) (address / 8);
		int col = 1 + address % 8;*/
		doUpdate = false;
		doStorageUpdate = false;
		setValueAt(data, row, col);
		doStorageUpdate = true;
		doUpdate = true;
	}
}
