package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.MatteBorder;

import de.teamwaldstadt.picsimu.Main;

public class GUIWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	int WIDTH = 800;
	int HEIGHT = 600;
	
	public GUIWindow() {
		setSize(WIDTH, HEIGHT);
		setTitle(Main.PGM_NAME + " " + Main.PGM_VERSION);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.GRAY);
		menuBar.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		JMenu menu = new JMenu("Datei");
		
		JMenuItem itemOpen = new JMenuItem("�ffnen");
		menu.add(itemOpen);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		setResizable(false);
		setContentPane(new GUIPanel(WIDTH, HEIGHT - 50));
		
		
		
		setVisible(true);
	}
}
