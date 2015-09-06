package net.alcuria.umbracraft.definitions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Defines a list of some Definition. Contains methods to modify that list.
 * @author Andrew Keturi
 * @param <T> the type of definition */
public class ListDefinition<T extends Definition> extends Definition {
	private ObjectMap<String, T> definitions;
	private int nextId;

	/** Adds an item to the list
	 * @param item */
	public void add(T item) {
		if (definitions == null) {
			definitions = new ObjectMap<>();
		}
		definitions.put(item.getName(), item);
	}

	/** Deletes an item from the list
	 * @param item the item to delete */
	public void delete(T item) {
		if (definitions == null) {
			return;
		}
		definitions.remove(item.getName());
	}

	/** Convenience method to fetch a {@link Definition} from the list. May be
	 * <code>null</code> and doesn't do any bounds checking.
	 * @param i the index
	 * @return a {@link Definition} */
	public Definition get(String key) {
		return definitions != null ? definitions.get(key) : null;
	}

	@Override
	public String getName() {
		return "List Definition";
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
		return definitions != null ? definitions.size : 0;
	}
}