package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.MatteBorder;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class GUIWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	int WIDTH = 800;
	int HEIGHT = 600;
	GUIPanel guiPanel;

	public GUIWindow(CodeExecutor codeExecutor) {

		guiPanel = new GUIPanel(WIDTH, HEIGHT - 50, codeExecutor);

		setSize(WIDTH, HEIGHT);
		setTitle(Main.PGM_NAME + " " + Main.PGM_VERSION);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setJMenuBar(initializeMenuBar(codeExecutor));
		setContentPane(guiPanel);
		setVisible(true);

	}

	public GUIPanel getPanel() {
		return this.guiPanel;
	}

	public JMenuBar initializeMenuBar(CodeExecutor codeExecutor) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.GRAY);
		menuBar.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		JMenu menu = new JMenu("Datei");

		JMenuItem itemOpen = new JMenuItem("\u00d6ffnen");
		itemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(GUIWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					codeExecutor.loadFile(fc.getSelectedFile());
				}
			}
		});
		menu.add(itemOpen);

		JMenuItem itemClose = new JMenuItem("Beenden");
		itemClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(itemClose);

		menuBar.add(menu);
		return menuBar;
	}
}
