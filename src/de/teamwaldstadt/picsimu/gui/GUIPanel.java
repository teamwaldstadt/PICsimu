package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.storage.SpecialRegister;


public class GUIPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	StorageTable storageTable;
	CodeView codeView;
	CodeExecutor codeExecutor;
	JInfoTable infoTable;
	List<JRegisterTable> registerTables;
	
	public GUIPanel(int width, int height, CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		
		/*
		 * general settings for the main panel 
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		/*
		 * the storage table, positioned at the right
		 */
		c.gridy = 0;
		c.gridx = 2;
		c.gridheight = 2;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		JScrollPane scrollTable = new JScrollPane();
		storageTable = new StorageTable(codeExecutor);
		scrollTable.setViewportView(storageTable);
		scrollTable.setPreferredSize(new Dimension(250, 300));
		scrollTable.setMinimumSize(new Dimension(250, 300));
		scrollTable.setMaximumSize(new Dimension(250, 300));
		add (scrollTable, c);
		
		/*
		 * the code view, positioned at the bottom
		 */
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
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
		add(scrollCode, c);
		
		/*
		 * the registers, positioned at the left
		 */
		c.gridy = 0;
		c.gridx = 0;
		c.gridheight = 2;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		JScrollPane registerScroll = new JScrollPane();
		JPanel registers = new JPanel();
		int space = 10;
		registerTables = new ArrayList<>();
		registers.setLayout(new BoxLayout(registers, BoxLayout.PAGE_AXIS));
		
		/* PORT A */
		JRegisterTable specialRegsRA = new JRegisterTable("RA", codeExecutor, SpecialRegister.PORTA);
		registers.add(specialRegsRA);
		registers.add(Box.createRigidArea(new Dimension(0, space)));
		registerTables.add(specialRegsRA);
		
		/* PORT B */
		JRegisterTable specialRegsRB = new JRegisterTable("RB", codeExecutor, SpecialRegister.PORTB);
		registers.add(specialRegsRB);
		registers.add(Box.createRigidArea(new Dimension(0, space)));
		registerTables.add(specialRegsRB);
		
		/* STATUS */
		JRegisterTable specialRegsStatus = new JRegisterTable("Status", codeExecutor, SpecialRegister.STATUS);
		registers.add(specialRegsStatus);
		registers.add(Box.createRigidArea(new Dimension(0, space)));
		registerTables.add(specialRegsStatus);
		
		/* Some special registers (W, PC, FSR, ...) */
		JPanel regInfoPanel = new JPanel();
		regInfoPanel.setBackground(Color.GREEN);
		regInfoPanel.setLayout(new BoxLayout(regInfoPanel, BoxLayout.LINE_AXIS));
		infoTable = new JInfoTable(codeExecutor);
		regInfoPanel.add(infoTable);
		registers.add(regInfoPanel);
		
		registerScroll.setViewportView(registers);
		registerScroll.setPreferredSize(new Dimension(250, 200));
		registerScroll.setMinimumSize(new Dimension(250, 200));
		registerScroll.setMaximumSize(new Dimension(250, 200));
		add(registerScroll, c);
		
		/*
		 * the buttons, positioned at the center
		 */
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.SOUTH;
		c.insets = new Insets(10, 10, 10, 10);
		JPanel buttons = new JPanel();
		buttons.setBackground(null);
		buttons.setPreferredSize(new Dimension(100, 40));
		buttons.setMinimumSize(new Dimension(100, 40));
		buttons.setMaximumSize(new Dimension(100, 40));
		buttons.setLayout(new GridLayout(1, 4, 20, 20));
		
		/* 'start' */ 
		JButton startButton = new JButton("Start");
		startButton.setFocusPainted(false);
		buttons.add(startButton);
		
		/* 'stop' */
		JButton stopButton = new JButton("Stop");
		stopButton.setFocusPainted(false);
		buttons.add(stopButton);
		
		/* 'reset' */
		JButton resetButton = new JButton("Reset");
		resetButton.setFocusPainted(false);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				codeExecutor.reset();
			}
		});
		buttons.add(resetButton);
		
		/* 'step' */
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
		
		/*
		 * additional features, positioned at the top center
		 */
		
		
		
		JPanel more = new JPanel();
		more.setLayout(new GridLayout(1, 2));
		JPanel freqSettings = new JPanel();
		JPanel freqSettings1 = new JPanel();
		
		TitledBorder border = new TitledBorder("Frequenz-Einstellungen");
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.TOP);
		border.setBorder(new MatteBorder(1, 1, 1, 1, new Color(150, 150, 150)));
		freqSettings1.setBorder(border);
		freqSettings.setLayout(new GridBagLayout());
		
		SpinnerNumberModel snm = new SpinnerNumberModel();
		snm.setStepSize(100000);
		snm.setMinimum(2000000);
		snm.setMaximum(5000000);
		JSpinner frequencySpinner = new JSpinner(snm);
		frequencySpinner.setPreferredSize(new Dimension(100, 30));
		frequencySpinner.setMinimumSize(new Dimension(100, 30));
		frequencySpinner.setValue(4000000);
		JLabel labelMhz = new JLabel("MHz");
		JLabel labelTitle = new JLabel("Quartz-Frequenz: ");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		c.gridy = 0;
		c.gridx = 0;
		c.weighty = 0;
		c.insets = new Insets(5, 2, 0, 2);
		labelTitle.setPreferredSize(new Dimension(110, 30));
		labelTitle.setMinimumSize(new Dimension(110, 30));
		freqSettings.add(labelTitle, c);
		c.gridx = 1;
		freqSettings.add(frequencySpinner, c);
		c.gridx = 2;
		freqSettings.add(labelMhz, c);
		
		JLabel labelFreqGen = new JLabel("Impuls-Generator: ");
		c.gridy = 1;
		c.gridx = 0;
		c.insets = new Insets(30, 2, 0, 2);
		freqSettings.add(labelFreqGen, c);
		c.insets = new Insets(0,2,0,2);
		c.gridx = 1;
		SpinnerNumberModel snm2 = new SpinnerNumberModel();
		snm2.setStepSize(10);
		snm2.setMinimum(10);
		snm2.setMaximum(100);
		JSpinner freqGenSpinner = new JSpinner(snm2);
		freqGenSpinner.setValue(20);
		freqSettings.add(freqGenSpinner, c);
		JLabel labelKhz = new JLabel("kHz");
		c.gridx = 2;
		freqSettings.add(labelKhz, c);
		
		JLabel labelFreqGenOut = new JLabel("Impuls-Output: ");
		c.gridy = 2;
		c.gridx = 0;
		freqSettings.add(labelFreqGenOut, c);
		String[] outValues = {"None", "RA0", "RA1", "RA2", "RA3", "RA4", "RA5", "RA6", "RA7", "RA8",};
		JComboBox<?> genOut = new JComboBox<>(outValues);
		c.gridx = 1;
		freqSettings.add(genOut, c);
		
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2, 2, 2, 2);
		c.gridx = 1;
		c.gridy = 0;
		freqSettings1.add(freqSettings);
		more.add(freqSettings1);
		
		JLabel label2 = new JLabel("");
		label2.setHorizontalAlignment(JLabel.CENTER);
		more.add(label2);
		add(more, c);
	}
	
	public CodeView getCodeView() {
		return codeView;
	}
	
	public StorageTable getStorageTable() {
		return storageTable;
	}

	public void updateRegistersView() {
		if (registerTables.isEmpty()) return;
		for (JRegisterTable t : registerTables) {
			t.update();
		}
		infoTable.updateGUI();
	}

}
