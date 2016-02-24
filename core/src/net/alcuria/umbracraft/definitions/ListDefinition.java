package net.alcuria.umbracraft.definitions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Defines a list of some Definition. Contains methods to modify that list.
 * @author Andrew Keturi
 * @param <T> the type of definition */
public class ListDefinition<T extends Definition> extends Definition {
	private ObjectMap<String, T> definitions = new ObjectMap<>();
	private int nextId;

	/** Adds an item to the list
	 * @param item */
	public void add(T item) {
		definitions.put(item.getName(), item);
	}

	public void delete(String key) {
		if (definitions.containsKey(key)) {
			definitions.remove(key);
		} else {
			System.err.println("Cannot delete. Key not found: " + key);
		}

	}

	/** Deletes an item from the list
	 * @param item the item to delete */
	public void delete(T item) {
		definitions.remove(item.getName());
	}

	/** Convenience method to fetch a {@link Definition} from the list. May be
	 * <code>null</code> and doesn't do any bounds checking.
	 * @param i the index
	 * @return a {@link Definition} */
	public Definition get(String key) {
		return definitions.get(key);
	}

	@Override
	public String getName() {
		return "List Definition";
	}

	@Override
	public String getTag() {
		return null;
	}

	public ObjectMap<String, T> items() {
		return definitions;
	}

	public Array<String> keys() {
		if (definitions == null) {
			return new Array<String>();
		}
		return definitions.keys().toArray();
	}

	public void setItems(ObjectMap<String, T> items) {
		this.definitions = items;
	}

	/** @return the size of the definition list */
	public int size() {
		return definitions.size;
	}
}