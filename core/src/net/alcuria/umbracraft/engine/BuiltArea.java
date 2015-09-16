package net.alcuria.umbracraft.engine;

import java.io.Serializable;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;

public class BuiltArea implements Serializable {

	private final AreaDefinition area;
	private boolean isRandom;
	private MapDefinition mapDefinition;
	private final AreaNodeDefinition node;
	private TeleportLocation north, south, east, west;

	public BuiltArea(AreaDefinition area, AreaNodeDefinition node) {
		if (area == null || node == null) {
			throw new NullPointerException("Area or node is null");
		}
		this.area = area;
		this.node = node;
		if (node.mapDefinition == null) {
			isRandom = true;
			mapDefinition = new MapDefinition();
			mapDefinition.setHeight(20);
			mapDefinition.setWidth(20); // TODO: generate map
		}
	}

	public void setTeleports(AreaBuilder allAreas) {
		if (node.teleport != null) {
			if (node.teleport.adjacentMaps.containsKey(TeleportDirection.NORTH.toString())) {
				final String northMapName = node.teleport.adjacentMaps.get(TeleportDirection.NORTH.toString());
				BuiltArea built = allAreas.getNode(area.name, northMapName);
				final MapDefinition northMapDef = Game.db().map(built.area.name);
				north = new TeleportLocation(northMapName, northMapDef.northX, northMapDef.northY);
			}
			if (node.teleport.adjacentMaps.containsKey(TeleportDirection.SOUTH.toString())) {
				final String southMapName = node.teleport.adjacentMaps.get(TeleportDirection.SOUTH.toString());
				BuiltArea built = allAreas.getNode(area.name, southMapName);
				final MapDefinition southMapDef = Game.db().map(built.area.name);
				south = new TeleportLocation(southMapName, southMapDef.southX, southMapDef.southY);
			}
			if (node.teleport.adjacentMaps.containsKey(TeleportDirection.EAST.toString())) {
				final String eastMapName = node.teleport.adjacentMaps.get(TeleportDirection.EAST.toString());
				BuiltArea built = allAreas.getNode(area.name, eastMapName);
				final MapDefinition eastMapDef = Game.db().map(built.area.name);
				east = new TeleportLocation(eastMapName, eastMapDef.eastX, eastMapDef.eastY);
			}
			if (node.teleport.adjacentMaps.containsKey(TeleportDirection.WEST.toString())) {
				final String westMapName = node.teleport.adjacentMaps.get(TeleportDirection.WEST.toString());
				BuiltArea built = allAreas.getNode(area.name, westMapName);
				final MapDefinition westMapDef = Game.db().map(built.area.name);
				west = new TeleportLocation(westMapName, westMapDef.westX, westMapDef.westY);
			}
		}
	}
}
