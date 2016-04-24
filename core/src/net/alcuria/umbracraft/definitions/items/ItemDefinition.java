package net.alcuria.umbracraft.definitions.items;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.party.PartyMember.EquipSlot;

/** Defines a collectible item.
 * @author Andrew Keturi */
public class ItemDefinition extends Definition {

	/** Enumerates the {@link ItemType#EQUIPMENT} type
	 * @author Andrew Keturi */
	public static enum EquipType {
		ACCESSORY("silhouette_ring"), SHOES("silhouette_shoe"), GLOVES("silhouette_fist"), HELM("silhouette_helm"), SHIELD("silhouette_shield"), TOP("silhouette_armor"), WEAPON("silhouette_sword");
		private final String emptyFilename;

		EquipType(String emptyFilename) {
			this.emptyFilename = emptyFilename;
		}

		public String getEmptyFilename() {
			return emptyFilename;
		}

		public EquipSlot getSlot() {
			switch (this) {
			case ACCESSORY:
				return EquipSlot.ACCESSORY_1; //TODO: intelligently determine which slot should be used here
			case SHOES:
				return EquipSlot.SHOES;
			case GLOVES:
				return EquipSlot.GLOVES;
			case HELM:
				return EquipSlot.HELM;
			case SHIELD:
				return EquipSlot.SHIELD;
			case TOP:
				return EquipSlot.TOP;
			case WEAPON:
				return EquipSlot.WEAPON;
			default:
				throw new NullPointerException("No slot type for " + this);
			}
		}
	}

	/** Enumerates different types of items.
	 * @author Andrew Keturi */
	public static enum ItemType {
		COLLECTIBLE(true), CONSUMABLE(true), EQUIPMENT(false), SCRIPT(true);

		private final boolean stackable;

		ItemType(boolean stackable) {
			this.stackable = stackable;
		}

		/** @return whether or not this item type is stackable */
		public boolean isStackable() {
			return stackable;
		}
	}

	/** Enumerates on all the secondary item stats that are added to gear
	 * @author Andrew Keturi */
	public static enum SecondaryStat {
		ACC("Hit"), ATK("Atk"), CRIT("Crit"), DEF("Def"), EVA("Evade"), FOC("Focus"), MATK("M.Atk"), MDEF("M.Def"), SPD("Speed");

		public static SecondaryStat[] sortedValues() {
			SecondaryStat[] stats = { ATK, MATK, DEF, MDEF, ACC, CRIT, SPD, EVA, FOC };
			return stats;
		}

		private final String name;

		SecondaryStat(String name) {
			this.name = name;
		}

		/** @return the baseline for which stats calculate from */
		public float baseline() {
			if (this == ACC || this == FOC || this == SPD) {
				return 1;
			}
			return 0;
		}

		/** Given an item definition, return its stat
		 * @param item the {@link ItemDefinition}
		 * @return the stat value */
		public float from(ItemDefinition item) {
			switch (this) {
			case ACC:
				return item.accuracy;
			case ATK:
				return item.atk;
			case CRIT:
				return item.critical;
			case DEF:
				return item.def;
			case EVA:
				return item.evasion;
			case FOC:
				return item.focus;
			case MATK:
				return item.matk;
			case MDEF:
				return item.mdef;
			case SPD:
				return item.speed;
			default:
				break;
			}
			throw new NullPointerException("Stat doesn't exist in ItemDefinition: " + this);
		}

		/** @return <code>true</code> if this should be displayed as a percentage */
		public boolean isPercent() {
			return this == SPD || this == EVA || this == ACC || this == FOC || this == CRIT;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Tooltip("Equip stats stats")
	@Order(200)
	public int atk, matk, def, mdef;
	@Tooltip("A description for the item")
	@Order(7)
	public String description;
	@Tooltip("The equipment type")
	@Order(6)
	public EquipType equipType;
	@Order(100)
	@Tooltip("HP recovered (flat amount)")
	public int hpRecovery;
	@Order(101)
	@Tooltip("HP recovered (percentage)")
	public float hpRecoveryPercent;
	@Tooltip("An icon to use in menus")
	@Order(8)
	public String icon;
	@Tooltip("The name of the item")
	@Order(1)
	public String name;
	@Tooltip("The item's price")
	@Order(4)
	public int price;
	@Order(102)
	@Tooltip("A script to call when invoking")
	public String script;
	@Tooltip("Secondary percent-based stats")
	@Order(201)
	public float speed, critical, accuracy, focus, evasion;
	@Tooltip("The tag, for sorting")
	@Order(2)
	public String tag;
	@Tooltip("The type of item")
	@Order(3)
	public ItemType type = ItemType.COLLECTIBLE;
	@Tooltip("The item's weight")
	@Order(5)
	public int weight;

	@Override
	public String getName() {
		return name != null ? name : "Enemy";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}

}
