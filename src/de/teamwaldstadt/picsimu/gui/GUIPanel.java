package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import de.teamwaldstadt.picsimu.CodeExecutor;


public class GUIPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	StorageTable storageTable;
	CodeView codeView;
	CodeExecutor codeExecutor;
	
	public GUIPanel(int width, int height, CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		//setBackground(new Color(255,255,255));
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		
		
		JScrollPane scrollTable = new JScrollPane();
		storageTable = new StorageTable();
		scrollTable.setViewportView(storageTable);
		
		//scrollTable.setBounds(width - 206, 0, 200, height);
		scrollTable.setPreferredSize(new Dimension(200, 300));
		scrollTable.setMinimumSize(new Dimension(200, 300));
		scrollTable.setMaximumSize(new Dimension(200, 300));
		add (scrollTable, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.4;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		
		JScrollPane scrollCode = new JScrollPane();
		codeView = new CodeView(codeExecutor);
	
		scrollCode.setViewportView(codeView);
		
		scrollCode.setPreferredSize(new Dimension(900, 250));
		scrollCode.setMinimumSize(new Dimension(900, 250));
		scrollCode.setMaximumSize(new Dimension(900, 250));
		scrollCode.setBackground(Color.GREEN);
		//scrollCode.setBounds(0, 300, width - 206, height - 300);
		add(scrollCode, c);
		
		c.gridy = 0;
		c.gridx = 0;
		c.gridheight = 1;
		c.weightx = 0.1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		
		JPanel registers = new JPanel();
		registers.setBorder(new MatteBorder(1,1,1,1, Color.BLACK));
		JLabel text = new JLabel("Register");
		registers.add(text);
		//registers.setBackground(Color.GREEN);
		registers.setPreferredSize(new Dimension(200, 200));
		registers.setMinimumSize(new Dimension(200, 200));
		registers.setMaximumSize(new Dimension(200, 200));
		add(registers, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.weightx = 1;
		
		c.anchor = GridBagConstraints.SOUTH;
		c.insets = new Insets(10, 10, 10, 10);
		JPanel buttons = new JPanel();
		buttons.setBackground(null);
		buttons.setPreferredSize(new Dimension(100, 40));
		buttons.setMinimumSize(new Dimension(100, 40));
		buttons.setMaximumSize(new Dimension(100, 40));
		buttons.setLayout(new GridLayout(1, 4, 20, 20));
		
		JButton startButton = new JButton("Start");
		startButton.setFocusPainted(false);
		buttons.add(startButton);
		
		JButton stopButton = new JButton("Stop");
		stopButton.setFocusPainted(false);
		buttons.add(stopButton);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setFocusPainted(false);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				codeExecutor.reset();
			}
		});
		buttons.add(resetButton);
		
		JButton stepButton = new JButton("Step");
		stepButton.setFocusPainted(false);
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				codeExecutor.nextCommand();
			}
		});
		buttons.add(stepButton);
		add(buttons, c);
	}
	
	public CodeView getCodeView() {
		return codeView;
	}
	
	public StorageTable getStorageTable() {
		return storageTable;
	}
}
