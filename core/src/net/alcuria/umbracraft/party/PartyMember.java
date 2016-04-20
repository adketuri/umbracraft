package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.items.ItemDefinition.EquipType;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.inventory.Inventory.ItemDescriptor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A member of the {@link Party}.
 * @author Andrew Keturi */
public class PartyMember {

	public static enum EquipSlot {
		ACCESSORY_1(EquipType.ACCESSORY), ACCESSORY_2(EquipType.ACCESSORY), BOTTOM(EquipType.BOTTOM), GLOVES(EquipType.GLOVES), HELM(EquipType.HELM), SHIELD(EquipType.SHIELD), TOP(EquipType.TOP), WEAPON(EquipType.WEAPON);
		private final EquipType type;

		EquipSlot(EquipType type) {
			this.type = type;
		}

		public EquipType getType() {
			return type;
		}
	}

	private transient Entity battler;
	private final ObjectMap<EquipSlot, ItemDescriptor> equipment = new ObjectMap<EquipSlot, ItemDescriptor>();
	private String heroId;
	private final Array<SkillDefinition> skills = new Array<SkillDefinition>();
	private MemberStats stats;

	/** For deserialization */
	public PartyMember() {
	}

	/** @param heroId the id of hero from the {@link HeroDefinition} */
	public PartyMember(final String heroId) {
		stats = new MemberStats(heroId);
		this.heroId = heroId;
	}

	/** Gets the battler set from battle
	 * @return the {@link Entity} battler */
	public Entity getBattler() {
		if (battler == null) {
			throw new NullPointerException("PartyMember's battler is null");
		}
		return battler;
	}

	/** @return the {@link HeroDefinition} used to define this party member */
	public HeroDefinition getDefinition() {
		return Game.db().hero(heroId);
	}

	public ObjectMap<EquipSlot, ItemDescriptor> getEquipment() {
		return equipment;
	}

	/** @return the {@link MemberStats} of this party member */
	public MemberStats getStats() {
		return stats;
	}

	/** Sets a reference to the entity used for battling. (Used elsewhere to
	 * reset stats after battle.)
	 * @param battler */
	public void setBattleEntity(Entity battler) {
		this.battler = battler;
	}

}
