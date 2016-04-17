package net.alcuria.umbracraft.definitions.items;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a collectible item.
 * @author Andrew Keturi */
public class ItemDefinition extends Definition {

	/** Enumerates the {@link ItemType#EQUIPMENT} type
	 * @author Andrew Keturi */
	public static enum EquipType {
		ACCESSORY, BOTTOM, HELM, SHIELD, TOP, WEAPON
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
