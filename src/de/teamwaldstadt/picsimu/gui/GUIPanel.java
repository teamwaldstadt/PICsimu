package de.teamwaldstadt.picsimu.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class GUIPanel extends JPanel  implements ActionListener{

	private static final long serialVersionUID = 1L;

	StorageTable table;
	int counter = 0;
	public GUIPanel(int width, int height) {
		JScrollPane scrollTable = new JScrollPane();
		table = new StorageTable();
		scrollTable.setSize(200, 400);
		scrollTable.setViewportView(table);
		setLayout(null);
		scrollTable.setBounds(width - 206, 0, 200, height);
		add (scrollTable, BorderLayout.CENTER);
		
		Timer t = new Timer(1000, this);
		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//counter++;
		//table.getModel().setValueAt(counter, 4, 4);
	}
}
