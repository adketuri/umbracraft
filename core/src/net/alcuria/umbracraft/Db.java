package net.alcuria.umbracraft;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A database class to maintain all modules and their contents from disc
 * @author Andrew Keturi */
public final class Db {

	//	private AnimationListDefinition animations;
	private final Array<Definition> definitions;

	public Db() {
		// create the definition map
		definitions = new Array<Definition>();
		final ObjectMap<String, Class<? extends Definition>> defs = new ObjectMap<String, Class<? extends Definition>>();
		defs.put("animations", AnimationListDefinition.class);
		defs.put("animationgroup", ListDefinition.class);
		// deserialize all definitions
		Json json = new Json();
		for (String name : defs.keys()) {
			final FileHandle handle = Gdx.files.external("umbracraft/" + name + ".json");
			if (handle.exists()) {
				definitions.add(json.fromJson(defs.get(name), handle));
			}
		}
	}

	/** Finds and returns an animation definition. Throws a null pointer
	 * exception if not found.
	 * @param name the name of the animation definition */
	public AnimationDefinition anim(String name) {
		if (definitions != null) {
			for (Definition definition : definitions) {
				if (definition instanceof AnimationListDefinition) {
					for (AnimationDefinition anim : ((AnimationListDefinition) definition).animations) {
						if (name.equals(anim.name)) {
							return anim;
						}
					}
				}
			}
		}
		throw new NullPointerException("Animation not found");
	}

	public AnimationGroupDefinition animGroup(String name) {
		if (definitions != null) {
			for (Definition definition : definitions) {
				if (definition instanceof ListDefinition<?>) {
					for (Definition list : ((ListDefinition<?>) definition).items()) {
						if (list instanceof AnimationGroupDefinition && ((AnimationGroupDefinition) list).name != null && ((AnimationGroupDefinition) list).name.equals(name)) {
							return (AnimationGroupDefinition) list;
						}
					}
				}
			}
		}
		throw new NullPointerException("Animation not found");
	}
}
