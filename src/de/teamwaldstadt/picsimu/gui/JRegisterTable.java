package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;

public class JRegisterTable extends JTable {

	private static final long serialVersionUID = 1L;

	CodeExecutor codeExecutor;
	SpecialRegister reg;
	DefaultTableModel tm;

	public JRegisterTable(String name, CodeExecutor codeExecutor, SpecialRegister reg) {
		this.codeExecutor = codeExecutor;
		this.reg = reg;
		tm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		init(name);
		if (reg == SpecialRegister.STATUS) {
			tm.setRowCount(2);
			String[] values = { "IRP", "RP1", "RP0", "TO", "PD", "Z", "DC", "C" };
			for (int i = 8; i > 0; i--) {
				setValueAt(values[8 - i], 0, 8 - i + 1);
			}
			setValueAt("Bit", 1, 0);
		} else if (reg == SpecialRegister.OPTION_REG) {
			tm.setRowCount(2);
			String[] values = { "RBP", "IntEdg", "TOCS", "TOSE", "PSA", "PS2", "PS1", "PS0" };
			for (int i = 8; i > 0; i--) {
				setValueAt(values[8 - i], 0, 8 - i + 1);
			}
			setValueAt("Bit", 1, 0);
		} else {
			for (int i = 8; i > 0; i--) {
				setValueAt(i - 1, 0, 8 - i + 1);
			}
			setValueAt("Tris", 1, 0);
			setValueAt("Pin", 2, 0);
		}
		update();
	}

	public void update() {
		//update bit and pin values (! not TRIS)
		int val = 0;
		try {
			val = Main.STORAGE.getRegister(reg.getAddress(), true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		for (int i = 8; i > 0; i--) {
			setValueAt(val & 0x01, getRowCount() - 1, i);
			val = val >> 1;
		}
		
		//update TRIS (only PORTA and PORTB)
		val = 0;
		switch (reg) {
		case PORTA:
			try {
				val = Main.STORAGE.getRegister(SpecialRegister.TRISA.getAddress(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case PORTB:
			try {
				val = Main.STORAGE.getRegister(SpecialRegister.TRISB.getAddress(), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case STATUS:
			return;
		default:
			return;
		}
		for (int i = 8; i > 0; i--) {
			setValueAt((val & 0x01) == 1 ? "i" : "o", 1, i);
			val = val >> 1;
		}
	}

	public void init(String name) {

		int width = 20;
		tm.setColumnCount(9);
		setModel(tm);

		getColumnModel().getColumn(0).setPreferredWidth(width + 11);
		getColumnModel().getColumn(1).setPreferredWidth(width);
		getColumnModel().getColumn(2).setPreferredWidth(width);
		getColumnModel().getColumn(3).setPreferredWidth(width);
		getColumnModel().getColumn(4).setPreferredWidth(width);
		getColumnModel().getColumn(5).setPreferredWidth(width);
		getColumnModel().getColumn(6).setPreferredWidth(width);
		getColumnModel().getColumn(7).setPreferredWidth(width);
		getColumnModel().getColumn(8).setPreferredWidth(width);

		tm.setRowCount(3);

		setTableHeader(null);
		setCellSelectionEnabled(false);

		setFont(new Font("default", Font.PLAIN, 11));

		setRowHeight(width);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int col = columnAtPoint(e.getPoint());
				if (row == 0 || col == 0)
					return;
				if (row != getRowCount() - 1)
					return;

				if (reg == SpecialRegister.PORTA || reg == SpecialRegister.PORTB) {
					try {
						int trisbit = (Main.STORAGE.getRegister(reg.getAddress() + 0x80, false) >> (8 - col)) & 1;
						if (trisbit == 0) {
							return;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				setValueAt(Integer.parseInt(String.valueOf(getValueAt(row, col))) ^ 1, row, col);

				int value = 0;
				for (int i = 0; i < 8; i++) {
					value = (value << 1) + Integer.parseInt(String.valueOf(getValueAt(row, i + 1)));
				}

				try {
					Main.STORAGE.setRegister(reg.getAddress(), value, true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				codeExecutor.updateStorage();
			}
		});
		setGridColor(Color.GRAY);
		setValueAt(name, 0, 0);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		if (row == 0 || row == getRowCount() - 2 || column == 0) {
			
			setBackground(GUIColor.DISABLED_TABLE_CELL.getColor());
			return super.prepareRenderer(renderer, row, column);
		}
		
		if (row == 2 && column >= 1 && getValueAt(row - 1,  column).equals("o")) {
			setBackground(GUIColor.NO_EDIT_COLOR.getColor());
			return super.prepareRenderer(renderer, row, column);
		}

		setBackground(GUIColor.CELL_BACKGROUND.getColor());

		Component c = super.prepareRenderer(renderer, row, column);
		c.setForeground(GUIColor.TEXT_COLOR.getColor());
		Object value = getValueAt(row, column);
		if (value != null && c instanceof JLabel) {
			JLabel l = (JLabel) c;
			String tooltip = null;
			if (String.valueOf(value).contains(";"))
				tooltip = String.valueOf(value).replaceAll(".*;", "");
			l.setToolTipText(tooltip);

		}
		return c;
	}
}
