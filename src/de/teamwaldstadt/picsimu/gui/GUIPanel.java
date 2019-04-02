package de.teamwaldstadt.picsimu.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import de.teamwaldstadt.picsimu.gui.storagetable.StorageTable;

public class GUIPanel extends JPanel  implements ActionListener{

	private static final long serialVersionUID = 1L;

	StorageTable storageTable;
	int counter = 0;
	CodeView codeView;
	
	public GUIPanel(int width, int height) {
		JScrollPane scrollTable = new JScrollPane();
		storageTable = new StorageTable();
		scrollTable.setViewportView(storageTable);
		setLayout(null);
		scrollTable.setBounds(width - 206, 0, 200, height);
		add (scrollTable);
		
		
		JScrollPane scrollCode = new JScrollPane();
		codeView = new CodeView();
		scrollCode.setViewportView(codeView);
		scrollCode.setBounds(width / 2 - 180, 300, 360, height - 310);
		add(scrollCode);
		
		JButton startButton = new JButton("Start");
		startButton.setBounds(width / 2 - 180, height / 2 - 20, 80, 30);
		add(startButton);
		
		JButton stopButton = new JButton("Stop");
		stopButton.setBounds(width / 2 - 87, height / 2 - 20, 80, 30);
		add(stopButton);
		
		JButton resetButton = new JButton("Reset");
		resetButton.setBounds(width / 2 + 7, height / 2 - 20, 80, 30);
		add(resetButton);
		
		JButton stepButton = new JButton("Step");
		stepButton.setBounds(width / 2 + 100, height / 2 - 20, 80, 30);
		add(stepButton);
		
		Timer t = new Timer(1000, this);
		t.start();
	}

	public CodeView getCodeView() {
		return this.codeView;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		storageTable.update();
	}
}
