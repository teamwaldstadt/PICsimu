package de.teamwaldstadt.picsimu.gui;

import java.awt.Color;

public enum GUIColor {
	DISABLED_TABLE_CELL (new Color(192,192,192)), 
	CELL_BACKGROUND (new Color(255,255,255)), 
	ALTERNATE_CELL_BACKGROUND (new Color(240,240,240)), 
	TEXT_COLOR (new Color(0,0,0)),
	NO_EDIT_COLOR (new Color(230, 230 ,230)),
	CELL_HIGHLIGHT_BACKGROUND (Color.YELLOW),
	CELL_HIGHLIGHT_FOREGROUND (Color.BLACK);
	
	Color color;
	Color defaultColor;
	
	private GUIColor(Color c) {
		this.color = c;
		this.defaultColor = c;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color c) {
		this.color = c;
	}
	public void resetColor() {
		this.color = defaultColor;
	}
	
}
