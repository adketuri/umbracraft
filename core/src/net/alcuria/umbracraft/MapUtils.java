package net.alcuria.umbracraft;

import com.badlogic.gdx.graphics.Color;

/** Utility functions to aid in map rendering
 * @author Andrew Keturi */
public class MapUtils {

	public static Color getTerrainColor(int terrain) {
		switch (terrain) {
		case 1:
			return Color.YELLOW;
		case 2:
			return Color.SKY;
		case 3:
			return Color.LIME;
		case 4:
			return Color.CORAL;
		case 5:
			return Color.MAGENTA;
		default:
			return Color.WHITE;
		}
	}
}
