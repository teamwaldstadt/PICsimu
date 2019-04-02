package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import de.teamwaldstadt.picsimu.Main;

public class GUIWindow extends JFrame {
	public GUIWindow() {
		setSize(800, 600);
		setTitle(Main.PGM_NAME + " " + Main.PGM_VERSION);
		setLocationRelativeTo(null);
		//setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.GRAY);
		menuBar.setBorder(new MatteBorder(0, 0, 2, 0, Color.BLACK));
		JMenu menu = new JMenu("Datei");
		
		JMenuItem itemOpen = new JMenuItem("Öffnen");
		menu.add(itemOpen);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		
		setContentPane(new GUIPanel());
		
		
		
		setVisible(true);
	}
}
