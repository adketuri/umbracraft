package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;

/** Builds areas/maps from the definitions for use in the engine.
 * @author Andrew Keturi */
public class AreaBuilder {
	private static final String FILENAME = "built";
	private ObjectMap<String, BuiltArea> areas;

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

	public void load() {

	}

	public void save() {

	}
}
