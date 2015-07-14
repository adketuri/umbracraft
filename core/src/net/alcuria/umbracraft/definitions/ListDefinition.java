package net.alcuria.umbracraft.definitions;

import com.badlogic.gdx.utils.Array;

/** Defines a list of some Definition. Contains methods to modify that list.
 * @author Andrew Keturi
 * @param <T> the type of definition */
public class ListDefinition<T extends Definition> extends Definition {
	private Array<T> definitions;
	private int nextId;

	/** Adds an item to the list
	 * @param item */
	public void add(T item) {
		if (definitions == null) {
			definitions = new Array<T>();
		}
		item.id = nextId++;
		definitions.add(item);
	}

	/** Deletes an item from the list
	 * @param item the item to delete */
	public void delete(T item) {
		if (definitions != null) {
			definitions.removeValue(item, true);
		}
	}

	/** Convenience method to fetch a {@link Definition} from the list. May be
	 * <code>null</code> and doesn't do any bounds checking.
	 * @param i the index
	 * @return a {@link Definition} */
	public Definition get(int i) {
		return definitions != null ? definitions.get(i) : null;
	}

	@Override
	public String getName() {
		if (definitions != null && definitions.size > 0) {
			return definitions.get(0).getName();
		}
		return "List Definition";
	}

	/** @return all definitions */
	public Array<T> items() {
		return definitions;
	}

	/** @return the size of the definition list */
	public int size() {
		return definitions != null ? definitions.size : 0;
	}
}