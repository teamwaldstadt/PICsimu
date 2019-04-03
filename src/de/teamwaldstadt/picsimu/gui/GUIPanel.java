package de.teamwaldstadt.picsimu.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import de.teamwaldstadt.picsimu.Main;
import de.teamwaldstadt.picsimu.command.CommandSet;
import de.teamwaldstadt.picsimu.gui.storagetable.StorageTable;
import de.teamwaldstadt.picsimu.parser.Parser;


public class GUIPanel extends JPanel  implements ActionListener{

	private static final long serialVersionUID = 1L;

	int commandNr;
	
	StorageTable storageTable;
	CodeView codeView;
	
	
	public GUIPanel(int width, int height) {
		commandNr = 0;
		JScrollPane scrollTable = new JScrollPane();
		storageTable = new StorageTable();
		scrollTable.setViewportView(storageTable);
		setLayout(null);
		scrollTable.setBounds(width - 206, 0, 200, height);
		add (scrollTable);
		
		
		JScrollPane scrollCode = new JScrollPane();
		codeView = new CodeView();
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
				commandNr = 0;
				gotoNextCommand();
				Main.STORAGE.resetAll();
				storageTable.update();
			}
		});
		add(resetButton);
		
		JButton stepButton = new JButton("Step");
		stepButton.setBounds(width / 2 + 100, height / 2 - 20, 80, 30);
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gotoNextCommand();
			}
		});
		add(stepButton);
		
		Timer t = new Timer(1000, this);
		t.start();
		
	}
	CommandSet[] commands;
	public void open(File file) {
		this.codeView.loadCode(file);
		commands = null;
		try {
			commands = Parser.getCommandList(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		gotoNextCommand();
	}
	
	public void gotoNextCommand() {
		if (commands == null || commands.length == 0)
			return;
		if (commandNr >= commands.length) 
			commandNr = 0;
		codeView.setLine(commands[commandNr].getLineNr() - 1);
		commandNr++;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//storageTable.update();
	}
}
