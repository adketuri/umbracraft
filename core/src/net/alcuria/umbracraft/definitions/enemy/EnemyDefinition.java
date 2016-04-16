package net.alcuria.umbracraft.definitions.enemy;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

public class EnemyDefinition extends Definition {

	@Tooltip("The BattleAnimationGroup")
	@Order(3)
	public String animGroup;
	@Tooltip("Secondary stats")
	@Order(7)
	public int atk, matk, def, mdef;
	@Tooltip("Max HP of the monster")
	@Order(5)
	public int hp;
	@Tooltip("Level of the monster")
	@Order(4)
	public int level;
	@Tooltip("The name of the monster")
	@Order(1)
	public String name;
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
