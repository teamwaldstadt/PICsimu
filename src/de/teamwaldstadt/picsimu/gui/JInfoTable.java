package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class JInfoTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	JInfoTableModel tm;
	public JInfoTable(CodeExecutor codeExecutor) {
		tm = new JInfoTableModel(codeExecutor);
		int width = 20;
		tm.setColumnCount(2);
		setModel(tm);

		getColumnModel().getColumn(0).setPreferredWidth(width);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		tm.setRowCount(3);
		setTableHeader(null);
		setCellSelectionEnabled(false);
		
		setModel(tm);
		setRowHeight(width);
	
		setGridColor(Color.GRAY);
		setModel(tm);
		setValueAt("W", 0, 0);
		setValueAt("PC", 1, 0);
		setValueAt("FSR", 2, 0);
		update();
	}
	
	public void update() {
		tm.setValueJustForGUI(String.format("%02X", Main.STORAGE.getW()), 0, 1);
		try {
			tm.setValueJustForGUI(String.format("%04X", Main.STORAGE.getPC()), 1, 1);
			tm.setValueJustForGUI(String.format("%02X", Main.STORAGE.getRegister(SpecialRegister.FSR.getAddress())), 2, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    	if (row % 2 == 1)
    		setBackground(Color.WHITE);
    	else 
    		setBackground(new Color(240, 240, 240));
		Component c = super.prepareRenderer(renderer, row, column);
        return c;
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
}
