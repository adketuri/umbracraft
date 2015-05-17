package net.alcuria.umbracraft;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/** A database class to maintain all modules and their contents from disc
 * @author Andrew Keturi */
public final class Db {

	private AnimationListDefinition animations;

	public Db() {
		Json json = new Json();
		final FileHandle handle = Gdx.files.external("umbracraft/animations.json");
		if (handle.exists()) {
			animations = json.fromJson(AnimationListDefinition.class, handle);
		}
	}

	/** Finds and returns an animation definition. Throws a null pointer
	 * exception if not found.
	 * @param name the name of the animation definition */
	public AnimationDefinition anim(String name) {
		if (animations != null) {
			for (AnimationDefinition anim : animations.animations) {
				if (name.equals(anim.name)) {
					return anim;
				}
			}
		}
		throw new NullPointerException("Animation not found");
	}
}
