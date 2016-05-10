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
import net.alcuria.umbracraft.definitions.enemy.EnemyDefinition;
import net.alcuria.umbracraft.definitions.enemy.EnemyGroupDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.modules.VariableDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.O;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A database class to maintain all {@link Definition} classes from disk. The
 * returned objects are never copied to new objects, so care should be taken
 * when modifying Definitions returned from this class. By default, the
 * {@link Game} and {@link Editor} contain a reference to a Db object, so any
 * additional copies are likely unnecessary. TODO: refactor this using generics,
 * enumerate all the types, or something.
 * @author Andrew Keturi */
@SuppressWarnings({ "unchecked", "rawtypes" })
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
		classes.put("skills", ListDefinition.class);
		classes.put("enemies", ListDefinition.class);
		classes.put("enemygroups", ListDefinition.class);
		classes.put("configuration", ConfigDefinition.class);
		classes.put("tilesets", TilesetListDefinition.class);
		classes.put("items", ListDefinition.class);

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

	/** @param name the id of an {@link AnimationCollectionDefinition}
	 * @return an {@link AnimationCollectionDefinition} from the db */
	public AnimationCollectionDefinition animCollection(String name) {
		return (AnimationCollectionDefinition) animCollections().get(name);
	}

	/** @return all {@link AnimationCollectionDefinition} objects in the db */
	public ListDefinition<AnimationCollectionDefinition> animCollections() {
		O.notNull(definitions);
		ListDefinition<AnimationCollectionDefinition> definition = (ListDefinition<AnimationCollectionDefinition>) definitions.get("animationcollection");
		return definition;
	}

	/** @param id the identifier of the {@link AnimationGroupDefinition}
	 * @return an {@link AnimationGroupDefinition} from the db */
	public AnimationGroupDefinition animGroup(String id) {
		return (AnimationGroupDefinition) animGroups().get(id);
	}

	/** @return all {@link AnimationGroupDefinition} objects in the db */
	public ListDefinition<AnimationGroupDefinition> animGroups() {
		O.notNull(definitions);
		ListDefinition<AnimationGroupDefinition> definition = (ListDefinition<AnimationGroupDefinition>) definitions.get("animationgroup");
		return definition;
	}

	/** @return all {@link AnimationDefinition} objects in the db */
	public ObjectMap<String, AnimationDefinition> anims() {
		O.notNull(definitions);
		return ((AnimationListDefinition) definitions.get("animations")).animations;
	}

	/** @param id the identifier of the area
	 * @return the {@link AreaDefinition} in the db */
	public AreaDefinition area(final String id) {
		return (AreaDefinition) areas().get(id);
	}

	/** @return all {@link AreaDefinition} objects */
	public ListDefinition<AreaDefinition> areas() {
		return (ListDefinition<AreaDefinition>) O.notNull(definitions).get("areas");
	}

	/** @param id the identifier in the db
	 * @return a single {@link BattleAnimationGroupDefinition} */
	public BattleAnimationGroupDefinition battleAnimGroup(String id) {
		return (BattleAnimationGroupDefinition) battleAnimGroups().get(id);
	}

	/** @return all {@link BattleAnimationGroupDefinition} objects in the db */
	public ListDefinition battleAnimGroups() {
		O.notNull(definitions);
		return (ListDefinition<BattleAnimationGroupDefinition>) definitions.get("battleanimationgroup");
	}

	/** @return the {@link ConfigDefinition} from the database. */
	public ConfigDefinition config() {
		return (ConfigDefinition) definitions.get("configuration");
	}

	/** @return all enemies */
	public ListDefinition enemies() {
		O.notNull(definitions);
		return (ListDefinition<EnemyDefinition>) definitions.get("enemies");
	}

	/** @param name the id of the {@link EnemyDefinition}
	 * @return a single {@link EnemyDefinition} */
	public EnemyDefinition enemy(String name) {
		return (EnemyDefinition) enemies().get(name);
	}

	/** @param id the id of the {@link EnemyGroupDefinition}
	 * @return the {@link EnemyGroupDefinition} */
	public EnemyGroupDefinition enemyGroup(String id) {
		return (EnemyGroupDefinition) enemyGroups().get(id);
	}

	/** @return all {@link EnemyGroupDefinition} objects in the db */
	public ListDefinition<EnemyGroupDefinition> enemyGroups() {
		O.notNull(definitions);
		return (ListDefinition<EnemyGroupDefinition>) definitions.get("enemygroups");

	}

	/** @return all {@link EntityDefinition} objects in the database. */
	public ListDefinition<EntityDefinition> entities() {
		O.notNull(definitions);
		return (ListDefinition<EntityDefinition>) definitions.get("entities");
	}

	/** @param id the {@link EntityDefinition}'s id
	 * @return an {@link Entity} in the db */
	public EntityDefinition entity(String id) {
		O.notNull(definitions);
		ListDefinition<EntityDefinition> definition = (ListDefinition<EntityDefinition>) definitions.get("entities");
		return (EntityDefinition) definition.get(id);
	}

	/** @param id the identifier of the flag
	 * @return the {@link FlagDefinition} in the db */
	public FlagDefinition flag(String id) {
		return (FlagDefinition) flags().get(id);
	}

	/** @return all {@link FlagDefinition} objects in the database */
	public ListDefinition<FlagDefinition> flags() {
		O.notNull(definitions);
		return (ListDefinition<FlagDefinition>) definitions.get("flags");
	}

	/** @param id the ID of a {@link HeroDefinition}
	 * @return the {@link HeroDefinition} with that ID in the database. */
	public HeroDefinition hero(String id) {
		return (HeroDefinition) heroes().get(id);
	}

	/** @return all {@link HeroDefinition} objects in the db */
	public ListDefinition<HeroDefinition> heroes() {
		O.notNull(definitions);
		ListDefinition<HeroDefinition> definition = (ListDefinition<HeroDefinition>) definitions.get("heroes");
		return definition;
	}

	/** @param id the {@link ItemDefinition} id
	 * @return the {@link ItemDefinition} in the db */
	public ItemDefinition item(String id) {
		return (ItemDefinition) items().get(id);
	}

	/** @return all {@link ItemDefinition} objects in the db */
	public ListDefinition<ItemDefinition> items() {
		O.notNull(definitions);
		return (ListDefinition<ItemDefinition>) definitions.get("items");
	}

	/** @param name the name of the map
	 * @return the {@link MapDefinition} from the database */
	public MapDefinition map(String name) {
		O.notNull(definitions);
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
		O.notNull(definitions);
		return (ListDefinition<ScriptDefinition>) definitions.get("scripts");
	}

	/** Gets a {@link SkillDefinition} from the DB
	 * @param id the skill's id
	 * @return the {@link SkillDefinition} */
	public SkillDefinition skill(String id) {
		return (SkillDefinition) skills().get(id);
	}

	/** @return all {@link SkillDefinition} objects in the database */
	public ListDefinition<SkillDefinition> skills() {
		O.notNull(definitions);
		return (ListDefinition<SkillDefinition>) definitions.get("skills");
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
		O.notNull(definitions);
		return (ListDefinition<VariableDefinition>) definitions.get("variables");
	}

}
