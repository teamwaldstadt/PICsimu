package de.teamwaldstadt.picsimu.gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class StorageTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	CodeExecutor codeExecutor;
	boolean doUpdate;
	public StorageTableModel(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		doUpdate = true;
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
		
		if (!value.matches("[0-9a-fA-F]{1,2}")) {
			JOptionPane.showMessageDialog(null, "Value not allowed!", "Alert", JOptionPane.CANCEL_OPTION); 
		} else {
			if (value.length() == 1) 
				value = "0" + value;
			super.setValueAt(value.toUpperCase(), row, col);
			Main.STORAGE.getStorage()[(col-1) + (getColumnCount() - 1) * (row-1)] = Integer.parseInt(value, 16);
			if (doUpdate) codeExecutor.updateRegisters();
		}
	}
	
	public void setValueWithoutRegisterUpdate(int address, int value) {
		int row = 1 + (int) (address / 8);
		int col = 1 + address % 8;
		doUpdate = false;
		setValueAt(String.format("%02X", value), row, col);
		doUpdate = true;
	}
}
