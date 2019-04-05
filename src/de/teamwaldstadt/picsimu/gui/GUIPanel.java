package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.gui.storagetable.StorageTable;


public class GUIPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	StorageTable storageTable;
	CodeView codeView;
	CodeExecutor codeExecutor;
	
	public GUIPanel(int width, int height, CodeExecutor codeExecutor) {
		this.codeExecutor = codeExecutor;
		setBackground(new Color(255,255,255));
		JScrollPane scrollTable = new JScrollPane();
		storageTable = new StorageTable();
		scrollTable.setViewportView(storageTable);
		setLayout(null);
		scrollTable.setBounds(width - 206, 0, 200, height);
		add (scrollTable);
		
		
		JScrollPane scrollCode = new JScrollPane();
		codeView = new CodeView(codeExecutor);
		
		scrollCode.setViewportView(codeView);
		scrollCode.setBounds(0, 300, width - 206, height - 300);
		add(scrollCode);
		
		JButton startButton = new JButton("Start");
		startButton.setBounds(width / 2 - 180, height / 2 - 20, 80, 30);
		add(startButton);
		
		JButton stopButton = new JButton("Stop");
		stopButton.setBounds(width / 2 - 87, height / 2 - 20, 80, 30);
		add(stopButton);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(width / 2 + 7, height / 2 - 20, 80, 30);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				codeExecutor.reset();
			}
		});
		add(resetButton);
		
		JButton stepButton = new JButton("Step");
		stepButton.setBounds(width / 2 + 100, height / 2 - 20, 80, 30);
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				codeExecutor.nextCommand();
			}
		});
		add(stepButton);
	}
	
	public CodeView getCodeView() {
		return codeView;
	}
	
	public StorageTable getStorageTable() {
		return storageTable;
	}
}
