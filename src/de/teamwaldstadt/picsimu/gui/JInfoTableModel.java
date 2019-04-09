package de.teamwaldstadt.picsimu.gui;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;
import de.teamwaldstadt.picsimu.utils.Utils;

public class JInfoTableModel extends DefaultTableModel {
	
	
	private static final long serialVersionUID = 1L;
	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 1 && row == 1) return false;
		return col != 0;
	}

	CodeExecutor codeExecutor;
	public JInfoTableModel(CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
	}
	
	@Override
	public void setValueAt(Object data, int row, int col) {
		String value = String.valueOf(data);
		
		//no action for first column (descriptions)
		if (col == 0) { 
			super.setValueAt(data, row, col); 
			return; 
		}
		
		//no action for second row (because PC is special)
		if (row == 1) {
			super.setValueAt(data, row, col);
			return;
		}
		
		//allow only valid byte values
		if (!Utils.isValidByte(value)) {
			JOptionPane.showMessageDialog(null, "Value not allowed!", "Alert", JOptionPane.CANCEL_OPTION); 
			return;
		}
		
		//prepend '0' if needed
		if (value.length() == 1) 
			value = "0" + value;
		
		//set WReg
		if (row == 0) {
			codeExecutor.updateWReg(Integer.parseInt(value, 16));
			super.setValueAt(value, row, col);
		}
		
		//set FSR
		if (row == 2) {
			try {
				Main.STORAGE.setRegister(SpecialRegister.FSR.getAddress(), Integer.parseInt(value, 16));
				super.setValueAt(value.toUpperCase(), row, col);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//update storage table gui
		codeExecutor.updateStorage();
	}
	
	
	public void setValue(String data, int row, int column) {
		super.setValueAt(data, row, column);
	}

}
