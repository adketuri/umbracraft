package net.alcuria.umbracraft.engine.inventory;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.definitions.items.ItemDefinition.ItemType;
import net.alcuria.umbracraft.util.O;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Maintains the state of the party's inventory
 * @author Andrew Keturi */
public class Inventory {

	/** Describes an item's attributes, for instance quantity or any special
	 * modifications to unique items.
	 * @author Andrew Keturi */
	public static class ItemDescriptor {
		private String id;
		private int quantity;

		/** For deserialization */
		public ItemDescriptor() {
		}

		/** @param id the ID of the item
		 * @param quantity the amount we want to add */
		public ItemDescriptor(String id, int quantity) {
			O.positive(quantity);
			this.id = id;
			this.quantity = quantity;
		}

		/** @return <code>true</code> if there are still items of this type in the
		 *         player's inventory */
		public boolean consume(int amount) {
			quantity -= amount;
			return quantity > 0;
		}

		public ItemDefinition getDefinition() {
			final ItemDefinition item = Game.db().item(id);
			if (item == null) {
				throw new NullPointerException("Cannot find item with id:" + id);
			}
			return item;
		}

		/** @return the id of this {@link ItemDescriptor} */
		public String getId() {
			return id;
		}

		/** @return the quantity of this descriptor */
		public int getQuantity() {
			return quantity;
		}

		public void increment(int amt) {
			quantity += amt;
		}
	}

	private int currentWeight;
	private final ObjectMap<String, Array<ItemDescriptor>> items = new ObjectMap<String, Array<ItemDescriptor>>();
	private int maxWeight = Game.db().config().maxWeight;
	private long money;

	/** Adds a descriptor to the items hash.
	 * @param descriptor */
	public void add(ItemDescriptor descriptor) {
		ItemDefinition item = Game.db().item(descriptor.getId());
		if (!items.containsKey(item.type.toString())) {
			items.put(item.type.toString(), new Array<ItemDescriptor>());
		}
		items.get(item.type.toString()).add(descriptor);
	}

	/** Adds an item to our inventory
	 * @param id the item's id
	 * @param quantity the amount to add
	 * @return <code>true</code> if adding was a success. adding may fail if the
	 *         inventory id is bad or the inventory is too full. */
	public boolean add(String id, int quantity) {
		// validation
		O.notNull(id);
		final ItemDefinition item = Game.db().item(id);
		if (item == null) {
			Game.error("Cannot add item " + id + ", not found in db");
			return false;
		}
		if (currentWeight + item.weight * quantity > maxWeight) {
			Game.error("Full capacity!");
			return false;
		}
		// see if we need to add a new key
		if (!items.containsKey(item.type.toString())) {
			items.put(item.type.toString(), new Array<ItemDescriptor>());
		}

		// insertion
		if (item.type.isStackable()) {
			boolean found = false;
			for (ItemDescriptor descriptor : items.get(item.type.toString())) {
				if (descriptor.id.equals(id)) {
					// update existing descriptor
					descriptor.increment(quantity);
					currentWeight += item.weight * quantity;
					found = true;
					break;
				}
			}
			if (!found) {
				// add new stack
				items.get(item.type.toString()).add(new ItemDescriptor(id, quantity));
				currentWeight += item.weight * quantity;
			}
		} else {
			// not stackable, let's add a whole bunch of instances
			while (quantity > 0) {
				items.get(item.type.toString()).add(new ItemDescriptor(id, 1));
				currentWeight += item.weight;
				quantity--;
			}
		}
		return true;
	}

	/** @return all the items in the player's inventory. Note, this returns the
	 *         internal array so modification should be done with care. */
	public Array<ItemDescriptor> get(ItemType type) {
		O.notNull(type);
		if (!items.containsKey(type.toString())) {
			items.put(type.toString(), new Array<ItemDescriptor>());
		}
		return items.get(type.toString());
	}

	/** @return the current weight capacity */
	public int getCurrentWeight() {
		return currentWeight;
	}

	/** @return the maximum weight capacity */
	public int getMaxWeight() {
		return maxWeight;
	}

	/** @return The amount of money in our inventory */
	public long getMoney() {
		return money;
	}

	/** Removes a descriptor
	 * @param descriptor */
	public void remove(ItemDescriptor descriptor) {
		items.get(descriptor.getDefinition().type.toString()).removeValue(descriptor, true);

	}

	/** Removes an item from our inventory. This should not do any partial
	 * removals if not enough items are present in the inventory to remove.
	 * @param id the item's id
	 * @param quantity the amount to remove
	 * @return <code>true</code> if the removal is successful */
	public boolean remove(String id, int quantity) {
		O.notNull(id);
		final ItemDefinition item = Game.db().item(id);
		if (item == null) {
			Game.error("Cannot remove item " + id + ", not found in db");
			return false;
		}

		// removal
		if (item.type.isStackable()) {
			boolean found = false;
			for (ItemDescriptor descriptor : items.get(item.type.toString())) {
				if (descriptor.id.equals(id)) {
					// update existing descriptor
					if (descriptor.quantity >= quantity) {
						boolean stillOwns = descriptor.consume(quantity);
						if (!stillOwns) {
							items.get(item.type.toString()).removeValue(descriptor, true);
						}
						currentWeight -= quantity * item.weight;
						found = true;
					} else {
						Game.error("Item was found, but not enought owned to remove " + id);
						return false;
					}
					break;
				}
			}
			if (!found) {
				Game.error("Item was not found to remove " + id);
				return false;
			}
		} else {
			// not stackable, count how many we have first
			int counted = 0;
			for (ItemDescriptor descriptor : items.get(item.type.toString())) {
				if (descriptor.id.equals(id)) {
					counted++;
				}
			}
			if (counted < quantity) {
				Game.error("Not enough nonstackables to remove " + id);
				return false;
			}
			// find instances and remove
			while (quantity > 0) {
				for (ItemDescriptor descriptor : items.get(item.type.toString())) {
					if (descriptor.id.equals(id)) {
						items.get(item.type.toString()).removeValue(descriptor, true);
						currentWeight -= item.weight;
						break;
					}
				}
			}
		}
		return true;
	}

	/** Resets the inventory, when reloading from disk
	 * @param otherItems the new inventory to use */
	public void reset(Inventory otherItems) {
		items.clear();
		if (otherItems == null) {
			return;
		}
		for (String key : otherItems.items.keys()) {
			items.put(key, otherItems.items.get(key));
		}
		currentWeight = otherItems.currentWeight;
		maxWeight = otherItems.maxWeight;
		money = otherItems.money;
	}

}
