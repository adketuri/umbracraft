package net.alcuria.umbracraft.definitions.enemy;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

public class EnemyGroupDefinition extends Definition {

	public Array<String> enemies;
	public String name;
	public String tag;

	@Override
	public String getName() {
		return name != null ? name : "Enemy";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}

}
