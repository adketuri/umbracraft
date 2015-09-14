package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;

/** A collection of utility functions for the {@link World}
 * @author Andrew Keturi */
public final class WorldUtils {
	/** Reads in the starting area/node from the db and fetches the
	 * {@link MapDefinition} name to use.
	 * @return the starting map name {@link String} */
	static String getStartingMapName() {
		final String startingArea = Game.db().config().startingArea;
		final String startingNode = Game.db().config().startingNode;
		final AreaDefinition areaDefinition = Game.db().area(startingArea);
		if (areaDefinition != null) {
			final AreaNodeDefinition areaNodeDefinition = areaDefinition.find(startingNode);
			if (areaNodeDefinition != null) {
				return areaNodeDefinition.mapDefinition;
			}
		}
		Game.error("Starting map was not found");
		return null;
	}
}
