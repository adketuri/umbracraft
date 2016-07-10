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
		switch (neighborMask) {
		case 0b0000_0000:
			throw new IllegalStateException("Tile marked as valid but has no valid adjacent tiles");
		}
		// each corner is concerned with its three neighboring tiles. To elaborate:
		// Corner 0 (Top Left)      | ?000_00??
		// Corner 1 (Top Right)     | ???0_0000
		// Corner 2 (Bottom Left)   | 0000_???0
		// Corner 3 (Bottom Right)  | 00??_?000

		for (int i = 0; i < corners.length; i++) {
			int edgeMask = neighborMask;
			// i'm not smart enough to do this without a switch
			// but the end goal is to get the bits we care about to the least significant digit
			switch (i) {
			case 0:
				edgeMask = edgeMask << 1;
				if ((edgeMask & 0b10000_000) > 0) {
					edgeMask++;
				}
				break;
			case 1:
				edgeMask = edgeMask >> 5;
				break;
			case 2:
				edgeMask = edgeMask >> 1;
		break;
		case 3:
			edgeMask = edgeMask >> 3;
			break;
		default:
			break;
			}

		}

		corners[0] = 14;
		corners[1] = 15;
		corners[2] = 20;
		corners[3] = 21;

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
