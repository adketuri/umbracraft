package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;

/** Builds areas/maps from the definitions for use in the engine.
 * @author Andrew Keturi */
public class AreaBuilder {
	private String currentArea, currentNode;

	/** Changes a node, updating the current map. Does not check if the direction
	 * is valid.
	 * @param direction */
	public void changeNode(TeleportDirection direction) {
		final AreaDefinition area = Game.db().area(currentArea);
		if (area.root == null || area.root.teleport == null) {
			throw new NullPointerException("Area does not have node/teleports");
		}
		final AreaNodeDefinition currentNodeDefinition = area.find(area.root, currentNode);
		String adjacentNodeName = currentNodeDefinition.teleport.adjacentMaps.get(direction.toString());
		AreaNodeDefinition adjacentNode = null;
		if (adjacentNodeName == null) {
			// no adjacent maps, but we need a reference to the parent here to see if teleporting back is possible
			final AreaNodeDefinition parentDefinition = area.findParent(area.root, currentNode);
			final String adjacentOppositeNode = parentDefinition.teleport.adjacentMaps.get(direction.opposite().toString());
			if (adjacentOppositeNode != null && adjacentOppositeNode.equals(currentNode)) {
				adjacentNode = area.find(area.root, parentDefinition.name);
				adjacentNodeName = parentDefinition.name;
			} else {
				throw new NullPointerException("Adjacent node " + adjacentNodeName + " could not be found in tree " + area.root.name);
			}
		} else {
			// teleport found in children so go for that
			adjacentNode = area.find(area.root, adjacentNodeName);
		}

		// create the map, get the starting coordinates
		Game.map().create(adjacentNode.mapDefinition);
		int newX = 0, newY = 0;
		switch (direction) {
		case EAST:
			newX = Game.db().map(adjacentNode.mapDefinition).westX * 16 + 12;
			newY = Game.db().map(adjacentNode.mapDefinition).westY * 16;
			break;
		case SOUTH:
			newX = Game.db().map(adjacentNode.mapDefinition).northX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).northY * 16 - 12;
			break;
		case NORTH:
			newX = Game.db().map(adjacentNode.mapDefinition).southX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).southY * 16 + 12;
			break;
		case WEST:
			newX = Game.db().map(adjacentNode.mapDefinition).eastX * 16 - 12;
			newY = Game.db().map(adjacentNode.mapDefinition).eastY * 16;
			break;
		}
		// add all map-specific entities to the map
		Game.entities().dispose(EntityScope.MAP);
		Game.entities().create(EntityScope.MAP, adjacentNode.mapDefinition);

		// set the player to his new position
		final Entity player = Game.entities().find(Entity.PLAYER);
		if (player != null) {
			player.position.set(newX, newY, player.position.z);
		}
		setAreaAndNode(currentArea, adjacentNodeName); //FIXME: area changes?
	}

	public void dispose() {

	}

	/** @return the current {@link AreaDefinition} id */
	public String getArea() {
		return currentArea;
	}

	/** @return the current {@link AreaNodeDefinition} id */
	public String getNode() {
		return currentNode;
	}

	/** Determines if from the current node the player is able to teleport in a
	 * particular direction.
	 * @param direction the {@link TeleportDirection} the player is trying to
	 *        teleport to
	 * @return <code>true</code> if a direction exists. */
	public boolean hasTeleportAt(TeleportDirection direction) {
		final AreaDefinition area = Game.db().area(currentArea);
		if (area.root == null || area.root.teleport == null) {
			return false; // no root
		}
		final AreaNodeDefinition currentNodeDefinition = area.find(area.root, currentNode);
		final String adjacentNodeName = currentNodeDefinition.teleport.adjacentMaps.get(direction.toString());
		if (adjacentNodeName == null) {
			// no adjacent maps, but we need a reference to the parent here to see if teleporting back is possible
			final AreaNodeDefinition parentDefinition = area.findParent(area.root, currentNode);
			if (parentDefinition != null && parentDefinition.teleport != null && parentDefinition.teleport.adjacentMaps != null) {
				final String adjacentOppositeNode = parentDefinition.teleport.adjacentMaps.get(direction.opposite().toString());
				if (adjacentOppositeNode != null && adjacentOppositeNode.equals(currentNode)) {
					return true; // teleport found!
				} else {
					return false; // no node found coming from parent, no path
				}
			}
			return false;
		}
		return true; // has a standard teleport to children
	}

	/** Updates the current area and node
	 * @param area
	 * @param node */
	public void setAreaAndNode(String area, String node) {
		currentArea = area;
		currentNode = node;
	}
}
