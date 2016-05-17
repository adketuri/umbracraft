package net.alcuria.umbracraft.definitions.enemy;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

public class EnemyDefinition extends Definition {

	public static enum PositionPreference {
		BACK, FRONT, MIDDLE;
	}

	@Tooltip("The BattleAnimationGroup")
	@Order(3)
	public String animGroup;
	@Tooltip("Secondary stats")
	@Order(7)
	public int atk, matk, def, mdef;
	@Tooltip("Drops for the monster")
	@Order(301)
	public String commonDrop, uncommonDrop, rareDrop, epicDrop;
	@Tooltip("Amount of exp earned for the party when killed")
	@Order(302)
	public int exp;
	@Tooltip("The face to use along the turn order table")
	@Order(10)
	public String faceId;
	@Tooltip("Max HP of the monster")
	@Order(5)
	public int hp;
	@Tooltip("Level of the monster")
	@Order(4)
	public int level;
	@Tooltip("Approximately how much money the enemy will drop")
	@Order(300)
	public int money;
	@Tooltip("The name of the monster")
	@Order(1)
	public String name;
	@Tooltip("The grid position preferences")
	@Order(9)
	public PositionPreference position;
	public Array<String> skills;
	@Tooltip("Additional percentage-based stats")
	@Order(8)
	public float speed, critical, accuracy, evasion, focus;
	@Tooltip("Base stats")
	@Order(6)
	public int str, wis, vit, agi;
	@Tooltip("The tag, for sorting")
	@Order(2)
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
