package de.teamwaldstadt.picsimu.gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class JInfoTableModel extends DefaultTableModel {
	
	
	private static final long serialVersionUID = 1L;
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 1 && row == 1) return false;
		return col != 0;
	}

	boolean doStorageUpdate;
	CodeExecutor codeExecutor;
	public JInfoTableModel(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		doStorageUpdate = true;
	}
	
	@Override
	public void setValueAt(Object data, int row, int col) {
		String value = String.valueOf(data);
		if (col == 0) { 
			super.setValueAt(data, row, col); 
			return; 
		}
		
		if (row == 1) {
			super.setValueAt(data, row, col);
			return;
		}
		
		if (!value.matches("[0-9a-fA-F]{1,2}")) {
			JOptionPane.showMessageDialog(null, "Value not allowed!", "Alert", JOptionPane.CANCEL_OPTION); 
		} else {
			if (value.length() == 1) 
				value = "0" + value;
			super.setValueAt(value.toUpperCase(), row, col);
			
			if (row == 0)
				codeExecutor.updateWReg(Integer.parseInt(value, 16));
			if (row == 2) {
				try {
					Main.STORAGE.setRegister(SpecialRegister.FSR.getAddress(), Integer.parseInt(value, 16));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (codeExecutor.gui != null && doStorageUpdate) {
					codeExecutor.gui.getStorageTable().updateWithoutRegisterUpdate();
				}
			}
		}
	}
	public void setValueJustForGUI(Object data, int row, int column) {
		doStorageUpdate = false;
		setValueAt(data, row, column);
		doStorageUpdate = true;
	}

}
