package net.alcuria.umbracraft;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.FlagDefinition;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.definitions.anim.BattleAnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.config.ConfigDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A database class to maintain all modules and their contents from disc
 * @author Andrew Keturi */
public final class Db {

	private final ObjectMap<String, Definition> definitions;

	public Db() {
		// create the definition map
		final ObjectMap<String, Class<? extends Definition>> classes = new ObjectMap<String, Class<? extends Definition>>();
		classes.put("animations", AnimationListDefinition.class);
		classes.put("animationgroup", ListDefinition.class);
		classes.put("battleanimationgroup", ListDefinition.class);
		classes.put("animationcollection", ListDefinition.class);
		classes.put("entities", ListDefinition.class);
		classes.put("map", ListDefinition.class);
		classes.put("areas", ListDefinition.class);
		classes.put("flags", ListDefinition.class);
		classes.put("configuration", ConfigDefinition.class);
		classes.put("tilesets", TilesetListDefinition.class);

		// deserialize all definitions
		definitions = new ObjectMap<>();
		Json json = new Json();
		for (String name : classes.keys()) {
			final FileHandle handle = Gdx.files.external("umbracraft/" + name + ".json");
			if (handle.exists() && Gdx.app.getType() == ApplicationType.Desktop) {
				definitions.put(name, json.fromJson(classes.get(name), handle));
			} else {
				final FileHandle internalHandle = Gdx.files.internal("db/" + name + ".json");
				if (internalHandle.exists()) {
					definitions.put(name, json.fromJson(classes.get(name), internalHandle));
				} else {
					throw new NullPointerException("Definition file is missing: " + name);
				}
			}
		}
	}

	/** Finds and returns an animation definition. Throws a null pointer
	 * exception if not found.
	 * @param name the name of the animation definition */
	public AnimationDefinition anim(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return ((AnimationListDefinition) definitions.get("animations")).animations.get(name);

	}

	public AnimationCollectionDefinition animCollection(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AnimationCollectionDefinition> definition = (ListDefinition<AnimationCollectionDefinition>) definitions.get("animationcollection");
		return (AnimationCollectionDefinition) definition.get(name);
	}

	public AnimationGroupDefinition animGroup(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AnimationGroupDefinition> definition = (ListDefinition<AnimationGroupDefinition>) definitions.get("animationgroup");
		return (AnimationGroupDefinition) definition.get(name);
	}

	public AreaDefinition area(final String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AreaDefinition> definition = (ListDefinition<AreaDefinition>) definitions.get("areas");
		return (AreaDefinition) definition.get(name);
	}

	public ListDefinition<AreaDefinition> areas() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AreaDefinition> definition = (ListDefinition<AreaDefinition>) definitions.get("areas");
		return definition;
	}

	public BattleAnimationGroupDefinition battleAnimGroup(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<BattleAnimationGroupDefinition> definition = (ListDefinition<BattleAnimationGroupDefinition>) definitions.get("battleanimationgroup");
		return (BattleAnimationGroupDefinition) definition.get(name);
	}

	public ConfigDefinition config() {
		return (ConfigDefinition) definitions.get("configuration");
	}

	public ListDefinition<EntityDefinition> entities() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return (ListDefinition<EntityDefinition>) definitions.get("entities");
	}

	public EntityDefinition entity(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<EntityDefinition> definition = (ListDefinition<EntityDefinition>) definitions.get("entities");
		return (EntityDefinition) definition.get(name);
	}

	public FlagDefinition flag(String id) {
		return (FlagDefinition) flags().get(id);
	}

	public ListDefinition<FlagDefinition> flags() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return (ListDefinition<FlagDefinition>) definitions.get("flags");

	}

	public MapDefinition map(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<MapDefinition> definition = (ListDefinition<MapDefinition>) definitions.get("map");
		return (MapDefinition) definition.get(name);
	}

	public TilesetDefinition tileset(int i) {
		TilesetListDefinition listDef = (TilesetListDefinition) definitions.get("tilesets");
		return listDef.tiles.get(i);
	}

}
