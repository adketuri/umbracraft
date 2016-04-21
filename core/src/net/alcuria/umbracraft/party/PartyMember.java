package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.definitions.items.ItemDefinition.EquipType;
import net.alcuria.umbracraft.definitions.items.ItemDefinition.SecondaryStat;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.inventory.Inventory.ItemDescriptor;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A member of the {@link Party}.
 * @author Andrew Keturi */
public class PartyMember {

	/** Enumerates an equipment slot in the equip screen.
	 * @author Andrew Keturi */
	public static enum EquipSlot {
		ACCESSORY_1, ACCESSORY_2, BOTTOM, GLOVES, HELM, SHIELD, TOP, WEAPON;

		public EquipType getType() {
			switch (this) {
			case ACCESSORY_1:
			case ACCESSORY_2:
				return EquipType.ACCESSORY;
			case BOTTOM:
				return EquipType.BOTTOM;
			case GLOVES:
				return EquipType.GLOVES;
			case HELM:
				return EquipType.HELM;
			case SHIELD:
				return EquipType.SHIELD;
			case TOP:
				return EquipType.TOP;
			case WEAPON:
				return EquipType.WEAPON;
			default:
				throw new NullPointerException("No case for EquipType" + this);
			}
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

	/** @param stat the stat to use for comparison
	 * @param comparisonItem the item we are using for comparison
	 * @return the difference between the passed in {@link ItemDefinition} and
	 *         the current character's equipment */
	public float getDeltaStat(SecondaryStat stat, ItemDefinition comparisonItem) {
		// no item passed in, delta is zero
		if (comparisonItem == null) {
			return 0;
		}
		// get the current sum of the stat in all slots
		float current = getSecondaryStat(stat);
		// get the value of the stat from the equipped variant
		float equippedValue = 0;
		for (ItemDescriptor equip : equipment.values()) {
			final ItemDefinition equippedItem = Game.db().item(equip.getId());
			if (equippedItem.equipType == comparisonItem.equipType) {
				equippedValue += stat.from(equippedItem);
			}
		}
		// get the value of the stat from the param
		float newValue = stat.from(comparisonItem);
		float updated = current - equippedValue + newValue;
		return updated - current;
	}

	/** @return the equipment {@link ObjectMap} */
	public ObjectMap<EquipSlot, ItemDescriptor> getEquipment() {
		return equipment;
	}

	/** Gets the value of a secondary stat from equips
	 * @param stat
	 * @return the value of the {@link SecondaryStat} */
	public float getSecondaryStat(SecondaryStat stat) {
		int total = 0;
		//add it up from gear
		for (ItemDescriptor equip : equipment.values()) {
			final ItemDefinition item = Game.db().item(equip.getId());
			total += stat.from(item);
		}
		return total;
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
