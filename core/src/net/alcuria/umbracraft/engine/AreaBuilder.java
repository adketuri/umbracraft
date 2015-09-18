package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

/** Builds areas/maps from the definitions for use in the engine.
 * @author Andrew Keturi */
public class AreaBuilder {
	private static final String FILENAME = "built";
	private ObjectMap<String, BuiltArea> areas;
	private String currentArea, currentNode;

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
		Game.map().create(adjacentNode.mapDefinition);
		int newX = 0, newY = 0;
		switch (direction) {
		case EAST:
			newX = Game.db().map(adjacentNode.mapDefinition).westX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).westY * 16;
			break;
		case SOUTH:
			newX = Game.db().map(adjacentNode.mapDefinition).northX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).northY * 16;
			break;
		case NORTH:
			newX = Game.db().map(adjacentNode.mapDefinition).southX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).southY * 16;
			break;
		case WEST:
			newX = Game.db().map(adjacentNode.mapDefinition).eastX * 16;
			newY = Game.db().map(adjacentNode.mapDefinition).eastY * 16;
			break;
		}
		Game.entities().find(Entity.PLAYER).position.x = newX;
		Game.entities().find(Entity.PLAYER).position.y = newY;
		setAreaAndNode(currentArea, adjacentNodeName); //FIXME: area changes?
	}

	public void create() {
		areas = new ObjectMap<>();
		for (AreaDefinition area : Game.db().areas().items().values()) {
			Game.log(area.name);
			for (AreaNodeDefinition node : area.getNodes()) {
				Game.log("  " + node.name);
				areas.put(area.name + "/" + node.name, new BuiltArea(area, node));
			}
		}
		for (BuiltArea area : areas.values()) {
			area.setTeleports(this);
		}
		Game.log("Done");
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(areas);
		Gdx.files.external("umbracraft/" + FILENAME + ".json").writeString(jsonStr, false);
	}

	public void dispose() {

	}

	public BuiltArea getNode(String area, String node) {
		return areas.get(area + "/" + node);
	}

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

	public void load() {

	}

	public void save() {

	}

	public void setAreaAndNode(String area, String node) {
		currentArea = area;
		currentNode = node;
	}
}
