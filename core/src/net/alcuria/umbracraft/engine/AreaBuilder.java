package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;

import com.badlogic.gdx.utils.ObjectMap;

/** Builds areas/maps from the definitions for use in the engine.
 * @author Andrew Keturi */
public class AreaBuilder {
	public static final String FILENAME = "built";
	public ObjectMap<String, BuiltArea> areas;

	public void create() {
		areas = new ObjectMap<>();
		for (AreaDefinition area : Game.db().areas().items().values()) {
			Game.log(area.name);
			for (AreaNodeDefinition node : area.getNodes()) {
				Game.log("  " + node.name);
				// add each area to the map: key = name, val = BuiltArea
				//// for each area, start at the root
				////// if it's defined, set the cardinal directions and build each child
				////// if random, use the direction from the parent to mark which edge is used
				////// look at each of the children's parent directions to see which are occupied
				////// if not enough free spaces, error
			}
		}
		Game.log("Done");
	}

	public void dispose() {

	}

	public void load() {

	}

	public void save() {

	}
}
