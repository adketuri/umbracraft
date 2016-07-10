package net.alcuria.umbracraft.util;

import com.badlogic.gdx.graphics.Color;

/** Utility functions to aid in map rendering
 * @author Andrew Keturi */
public class MapUtils {

	public static final Color BLUE = new Color(0, 0, 1, 0.5f);
	public static final Color CYAN = new Color(0, 1, 1, 0.5f);
	public static final Color GRAY = new Color(0, 0, 0, 0.5f);
	public static final Color GREEN = new Color(0, 1, 0, 0.5f);
	public static final Color MAGENTA = new Color(1, 0, 1, 0.5f);
	public static final Color RED = new Color(1, 0, 0, 0.5f);
	public static final Color YELLOW = new Color(1, 1, 0, 0.5f);

	public static Color getTerrainColor(int terrain) {
		switch (terrain) {
		case 1:
			return RED;
		case 2:
			return GREEN;
		case 3:
			return BLUE;
		case 4:
			return YELLOW;
		case 5:
			return MAGENTA;
		case 6:
			return CYAN;
		default:
			return Color.WHITE;
		}
	}
}
