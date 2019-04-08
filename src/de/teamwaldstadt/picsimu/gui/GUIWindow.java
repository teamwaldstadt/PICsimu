package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

	int WIDTH = 800;
	int HEIGHT = 600;
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
		
		setSize(WIDTH, HEIGHT);
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
					UIManager.put( "control", new Color( 128, 128, 128) );
					UIManager.put( "info", new Color(128,128,128) );
					UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
					UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
					UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
					UIManager.put( "nimbusFocus", new Color(115,164,209) );
					UIManager.put( "nimbusGreen", new Color(176,179,50) );
					UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
					UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
					UIManager.put( "nimbusOrange", new Color(191,98,4) );
					UIManager.put( "nimbusRed", new Color(169,46,34) );
					UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
					UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
					UIManager.put( "text", new Color( 230, 230, 230) );
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(frame);
				isDarkMode = !isDarkMode;
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
		return menuBar;
	}
}