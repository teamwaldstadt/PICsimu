package de.teamwaldstadt.picsimu.gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.utils.Utils;

public class StorageTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	CodeExecutor codeExecutor;
	
	public StorageTableModel(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return !(row == 0 || col == 0);
	}
	
	
	/*
	 *	convention: this method shall only be used for hand made table cell edits!!! 
	 */
	@Override
	public void setValueAt(Object data, int row, int col) {
		
		String value = String.valueOf(data);
		
		//disable edit of storage cell 0
		if (row == 1 && col == 1) {
			JOptionPane.showMessageDialog(null, "Not a physical register", "Error", JOptionPane.CANCEL_OPTION);
			return;
		}
		
		//allow only valid byte values
		if (!Utils.isValidByte(value)) {
			JOptionPane.showMessageDialog(null, "Value not allowed!", "Error", JOptionPane.CANCEL_OPTION);
			return;
		} 
			
		//prepend '0' if needed
		if (value.length() == 1) value = "0" + value;
		
		//try to set the register in the storage and update the table (for mirroring)
		try {
			Main.STORAGE.setRegister((col-1) + (getColumnCount() - 1) * (row-1), Integer.parseInt(value, 16));
			updateGUI();
			//the graphical value will only be set if there was no exception
			super.setValueAt(value.toUpperCase(), row, col);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.CANCEL_OPTION);
		}
		
		//tell the register views to update
		codeExecutor.updateRegisters();
	}
	
	private void setValue(int value, int address) {
		
		if (!Utils.isValidByte(value)) {
			Utils.log(this, "Value '" + value + "' is not a valid byte");
			return;
		}
		
		int row = (int) address / 8 + 1;
		int col = address % 8 + 1;

		super.setValueAt(String.format("%02X", value), row, col);
	}
	
	public void updateGUI() {
		for (int i = 0; i < Main.STORAGE.getStorage().length; i++) {
			try {
				setValue(Main.STORAGE.getRegister(i), i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void init() {
		for (int i = 1; i < 9; i++)
			super.setValueAt(String.format("%02X", (i-1)), 0, i);
		for (int i = 0; i < 33; i++)
			super.setValueAt(String.format("%02X", i * 8 - 8), i, 0);	
		super.setValueAt("", 0, 0);
	}
}
