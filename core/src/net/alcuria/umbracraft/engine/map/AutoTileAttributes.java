package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;

/** This object is created on a per-tile basis to store any terrain information
 * necessary to render an aut-tile
 * @author Andrew */
public class AutoTileAttributes {

	private final int[] corners = new int[4];
	private final TilesetDefinition tilesetDefinition;
	private int type;

	public AutoTileAttributes(TilesetDefinition tilesetDefinition) {
		this.tilesetDefinition = tilesetDefinition;
	}

	public int getCorner(int idx) {
		return corners[idx];
	}

	public int getType() {
		return type;
	}

	public boolean isInitialized() {
		return type > 0;
	}

	/** This passes in a bitmask describing the neighboring tiles. From that, we
	 * assign each of the autotile corners to their respective index. This index
	 * is not specific to any particular terrain, instead it's a very generic
	 * subtiling system as seen in RM2k3. I am not a smart man, so for now this
	 * is just going to be a huge switch. Thankfully, this is only called once
	 * upon map initialization so performance is not as critical.
	 * @param neighborMask the bitmask describing neighbors. Each bit
	 *        corresponds to a surrounding tile. Since there are 8 surrounding
	 *        tiles, masks are 8 bits long. The neighbor order used is: top
	 *        topright right rightdown _ down downleft left lefttop */
	public void setMask(int neighborMask) {
		if (type <= 0) {
			throw new IllegalStateException("Tile type must be set first");
		}

		// each corner is concerned with its three neighboring tiles. To elaborate:
		// Corner 0 (Top Left)      |   ?000_00??   |   LEFT, LEFT TOP, TOP
		// Corner 1 (Top Right)     |   ???0_0000   |   TOP, TOP_RIGHT, RIGHT
		// Corner 2 (Bottom Left)   |   0000_???0   |   BOTTOM, BOTTOM_LEFT, LEFT
		// Corner 3 (Bottom Right)  |   00??_?000   |   RIGHT, BOTTOM_RIGHT, BOTTOM

		for (int i = 0; i < corners.length; i++) {
			// i'm not smart enough to do this without a switch
			// but the end goal is to get the bits we care about to the least significant digits
			// an excercise for the reader: please make this cleaner for me and I'll buy you a coffee :)
			int edgeMask = neighborMask;
			if (i == 0) {
				edgeMask = edgeMask << 1;
				if ((edgeMask & 0b10000_0000) > 0) {
					edgeMask++;
				}
				edgeMask = edgeMask & 0b0000_0111;
				switch (edgeMask) {
				case 0b000:
				case 0b010:
					corners[i] = 12;
					break;
				case 0b001:
				case 0b011:
					corners[i] = 24;
					break;
				case 0b100:
				case 0b110:
					corners[i] = 14;
					break;
				case 0b111:
					corners[i] = 26;
					break;
				case 0b101:
					corners[i] = 4;
					break;
				}
			} else if (i == 1) {
				edgeMask = edgeMask >> 5;
				edgeMask = edgeMask & 0b0000_0111;
				switch (edgeMask) {
				case 0b000:
				case 0b010:
					corners[i] = 17;
					break;
				case 0b001:
				case 0b011:
					corners[i] = 15;
					break;
				case 0b100:
				case 0b110:
					corners[i] = 29;
					break;
				case 0b101:
					corners[i] = 5;
					break;
				case 0b111:
					corners[i] = 27;
					break;
				}
			} else if (i == 2) {
				edgeMask = edgeMask >> 1;
				edgeMask = edgeMask & 0b0000_0111;
				switch (edgeMask) {
				case 0b000:
				case 0b010:
					corners[i] = 42;
					break;
				case 0b001:
				case 0b011:
					corners[i] = 44;
					break;
				case 0b100:
				case 0b110:
					corners[i] = 30;
					break;
				case 0b101:
					corners[i] = 10;
					break;
				case 0b111:
					corners[i] = 32;
					break;
				}
			} else if (i == 3) {
				edgeMask = edgeMask >> 3;
				edgeMask = edgeMask & 0b0000_0111;

				switch (edgeMask) {
				case 0b000:
				case 0b010:
					corners[i] = 47;
					break;
				case 0b001:
				case 0b011:
					corners[i] = 35;
					break;
				case 0b100:
				case 0b110:
					corners[i] = 45;
					break;
				case 0b101:
					corners[i] = 11;
					break;
				case 0b111:
					corners[i] = 33;
					break;
				}
			}
		}

		/*
		 * corners[0] = 0; corners[1] = 0; corners[2] = 0; corners[3] = 0;
		 */
	}

	/** Given an ID from the editor (1-6) this sets the TYPE of terrain for the
	 * particular tilesetDefinition. This is how we decouple map data and
	 * tileset data.
	 * @param id the id from the map editor
	 * @param isOverlay if <code>true</code>, we'll use the overlay types */
	public void setType(int id, boolean isOverlay) {
		if (!isOverlay) {
			if (id == 1) {
				type = tilesetDefinition.terrain1;
			} else if (id == 2) {
				type = tilesetDefinition.terrain2;
			} else if (id == 3) {
				type = tilesetDefinition.terrain3;
			} else if (id == 4) {
				type = tilesetDefinition.terrain4;
			} else if (id == 5) {
				type = tilesetDefinition.stairs;
			} else if (id == 6) {
				type = tilesetDefinition.treeWall;
			}
		} else {
			if (id == 1) {
				type = tilesetDefinition.overlay;
			}
		}
	}

}
