package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import de.teamwaldstadt.picsimu.CodeExecutor;
import de.teamwaldstadt.picsimu.Main;

public class GUIWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	int WIDTH = 950;
	int HEIGHT = 720;
	int MIN_HEIGHT = 620;
	int MIN_WIDTH = 900;
	GUIPanel guiPanel;
	JFrame frame;
	boolean isDarkMode = false;
	
	public GUIWindow(CodeExecutor codeExecutor) {
		frame = this;
		guiPanel = new GUIPanel(WIDTH, HEIGHT - 50, codeExecutor);
		
		UIManager.put("Table.cellNoFocusBorder", new MatteBorder(0, 1, 1, 0, new Color(0, 0, 0, 0)));
		UIManager.put("Table.showGrid", true);
		UIManager.put("Table.focusCellHighlightBorder", new MatteBorder(1, 1, 2, 2, Color.BLACK));
		UIManager.put("Table.textForeground", new Color(0,0,0));
		
		try {
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		setTitle(Main.PGM_NAME + " " + Main.PGM_VERSION);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(initializeMenuBar(codeExecutor));
		setContentPane(guiPanel);
		SwingUtilities.updateComponentTreeUI(this);
		pack();
		setVisible(true);
		

	}

	public GUIPanel getPanel() {
		return this.guiPanel;
	}
	public JMenuBar initializeMenuBar(CodeExecutor codeExecutor) {
		JMenuBar menuBar = new JMenuBar();
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

		JCheckBoxMenuItem darkModeItem = new JCheckBoxMenuItem("Dark Mode");
		darkModeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isDarkMode) {
					Color dark = new Color(50,50,50);
					Color light = new Color(255,255,255);
					UIManager.put( "control", dark );
					UIManager.put( "info", dark);
					UIManager.put( "nimbusBase", dark );
					UIManager.put( "nimbusAlertYellow", light );
					UIManager.put( "nimbusDisabledText",dark );
					UIManager.put( "nimbusFocus", dark );
					UIManager.put( "nimbusGreen", dark);
					UIManager.put( "nimbusInfoBlue", light);
					UIManager.put( "nimbusLightBackground", new Color(100,100,100) );//text boxes background
					UIManager.put( "nimbusOrange", dark);
					UIManager.put( "nimbusRed", dark);
					UIManager.put( "nimbusSelectedText", dark );
					UIManager.put( "nimbusSelectionBackground", light );
					UIManager.put( "text", light );
				} else {
					UIManager.getDefaults().put( "control", new Color( 214, 217, 223) );
					UIManager.put( "info", new ColorUIResource(242,242,189) );
					UIManager.put( "nimbusBase", new ColorUIResource(51,98,140) );
					UIManager.put( "nimbusAlertYellow", new ColorUIResource(255,220,35) );
					UIManager.put( "nimbusDisabledText", new ColorUIResource(142,143,145) );
					UIManager.put( "nimbusFocus", new ColorUIResource(115,164,209) );
					UIManager.put( "nimbusGreen", new ColorUIResource(176,179,50) );
					UIManager.put( "nimbusInfoBlue", new ColorUIResource(47,92,180) );
					UIManager.put( "nimbusLightBackground", new ColorUIResource(255,255,255) );
					UIManager.put( "nimbusOrange", new ColorUIResource(191,98,4) );
					UIManager.put( "nimbusRed", new ColorUIResource(169,46,34) );
					UIManager.put( "nimbusSelectedText", new ColorUIResource(255,255,255) );
					UIManager.put( "nimbusSelectionBackground", new ColorUIResource(57,105,138) );
					UIManager.put( "text", new ColorUIResource( 0, 0, 0) );
				}
				try {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
				} catch (UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
				isDarkMode = !isDarkMode;
				guiPanel.toggleDarkMode();
			}
		});
		menu.add(darkModeItem);
				
		JMenuItem itemClose = new JMenuItem("Beenden");
		itemClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(itemClose);
		
		menuBar.add(menu);
		
		JMenu info = new JMenu("Info");
		
		JMenuItem about = new JMenuItem("\u00DCber");
		JMenuItem license = new JMenuItem("Lizenz");
		JMenuItem help = new JMenuItem("Dokumentation");
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, Main.PGM_NAME + " version " + Main.PGM_VERSION + "\n\nMade by:\nTeam Waldstadt (Jakob Gietl, Fynn Arnold)\n\nView source code at:\nhttps://github.com/teamwaldstadt/PICsimu", "\u00DCber", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		license.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Copyright \u00A9 2019 Team Waldstadt\n\nLicensed under the EUPL", "Lizenz", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Not implemented yet", "Dokumentation", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		info.add(about);
		info.add(license);
		info.add(help);
		menuBar.add(info);
		return menuBar;
	}
}