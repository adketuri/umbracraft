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
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.modules.VariableDefinition;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A database class to maintain all {@link Definition} classes from disk. The
 * returned objects are never copied to new objects, so care should be taken
 * when modifying Definitions returned from this class. By default, the
 * {@link Game} and {@link Editor} contain a reference to a Db object, so any
 * additional copies are likely unnecessary.
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
		classes.put("variables", ListDefinition.class);
		classes.put("scripts", ListDefinition.class);
		classes.put("heroes", ListDefinition.class);
		classes.put("configuration", ConfigDefinition.class);
		classes.put("tilesets", TilesetListDefinition.class);

		// deserialize all definitions
		definitions = new ObjectMap<>();
		Json json = new Json();
		json.setIgnoreUnknownFields(true);
		for (String name : classes.keys()) {
			final FileHandle handle = Gdx.files.external("umbracraft/" + name + ".json");
			if (handle.exists() && Gdx.app.getType() == ApplicationType.Desktop) {
				definitions.put(name, json.fromJson(classes.get(name), handle));
			} else {
				final FileHandle internalHandle = Gdx.files.internal("db/" + name + ".json");
				if (internalHandle.exists()) {
					definitions.put(name, json.fromJson(classes.get(name), internalHandle));
				} else {
					try {
						definitions.put(name, classes.get(name).newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/** Finds and returns an animation definition. Throws a null pointer
	 * exception if not found.
	 * @param name the name of the animation definition */
	public AnimationDefinition anim(String name) {
		return anims().get(name);
	}

	public AnimationCollectionDefinition animCollection(String name) {
		return (AnimationCollectionDefinition) animCollections().get(name);
	}

	public ListDefinition<AnimationCollectionDefinition> animCollections() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AnimationCollectionDefinition> definition = (ListDefinition<AnimationCollectionDefinition>) definitions.get("animationcollection");
		return definition;
	}

	public AnimationGroupDefinition animGroup(String id) {
		return (AnimationGroupDefinition) animGroups().get(id);
	}

	public ListDefinition<AnimationGroupDefinition> animGroups() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AnimationGroupDefinition> definition = (ListDefinition<AnimationGroupDefinition>) definitions.get("animationgroup");
		return definition;
	}

	public ObjectMap<String, AnimationDefinition> anims() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return ((AnimationListDefinition) definitions.get("animations")).animations;
	}

	public AreaDefinition area(final String id) {
		return (AreaDefinition) areas().get(id);
	}

	public ListDefinition<AreaDefinition> areas() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<AreaDefinition> definition = (ListDefinition<AreaDefinition>) definitions.get("areas");
		return definition;
	}

	public BattleAnimationGroupDefinition battleAnimGroup(String id) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<BattleAnimationGroupDefinition> definition = (ListDefinition<BattleAnimationGroupDefinition>) definitions.get("battleanimationgroup");
		return (BattleAnimationGroupDefinition) definition.get(id);
	}

	/** @return the {@link ConfigDefinition} from the database. */
	public ConfigDefinition config() {
		return (ConfigDefinition) definitions.get("configuration");
	}

	/** @return all {@link EntityDefinition} objects in the database. */
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

	/** @return all {@link FlagDefinition} objects in the database */
	public ListDefinition<FlagDefinition> flags() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return (ListDefinition<FlagDefinition>) definitions.get("flags");
	}

	/** @param id the ID of a {@link HeroDefinition}
	 * @return the {@link HeroDefinition} with that ID in the database. */
	public HeroDefinition hero(String id) {
		return (HeroDefinition) heroes().get(id);
	}

	public ListDefinition<HeroDefinition> heroes() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<HeroDefinition> definition = (ListDefinition<HeroDefinition>) definitions.get("heroes");
		return definition;
	}

	/** @param name the name of the map
	 * @return the {@link MapDefinition} from the database */
	public MapDefinition map(String name) {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		ListDefinition<MapDefinition> definition = (ListDefinition<MapDefinition>) definitions.get("map");
		return (MapDefinition) definition.get(name);
	}

	/** @param id A script ID
	 * @return the {@link ScriptDefinition} with that particular ID in the
	 *         database */
	public ScriptDefinition script(String id) {
		return (ScriptDefinition) scripts().get(id);
	}

	/** @return all {@link FlagDefinition} objects in the database */
	public ListDefinition<ScriptDefinition> scripts() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return (ListDefinition<ScriptDefinition>) definitions.get("scripts");
	}

	/** Gets a tileset TODO: refactor using keys
	 * @param i the index
	 * @return the {@link TilesetDefinition} */
	public TilesetDefinition tileset(int i) {
		TilesetListDefinition listDef = (TilesetListDefinition) definitions.get("tilesets");
		return listDef.tiles.get(i);
	}

	/** @param id a variable id
	 * @return a {@link VariableDefinition} with the given id */
	public VariableDefinition variable(String id) {
		return (VariableDefinition) variables().get(id);
	}

	/** @return all {@link VariableDefinition} objects in the database */
	public ListDefinition<VariableDefinition> variables() {
		if (definitions == null) {
			throw new NullPointerException("Definitions not initialized");
		}
		return (ListDefinition<VariableDefinition>) definitions.get("variables");
	}

}
