package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.engine.entities.BaseEntity;

import com.badlogic.gdx.utils.Array;

/** A manager manages a list of children, with methods to add and update them.
 * @author Andrew Keturi
 * @param <T> a child {@link BaseEntity} type */
public abstract class Manager<T extends BaseEntity> {
	Array<T> children;

	public void add(T child) {
		if (children == null) {
			children = new Array<T>();
		}
		children.add(child);
	}

	public void remove(T child) {
		if (children == null) {
			return;
		}
		children.removeValue(child, true);
	}

	public void render() {
		if (children == null) {
			return;
		}
		for (T child : children) {
			child.render();
		}
	}

	public void update() {
		if (children == null) {
			return;
		}
		for (T child : children) {
			child.update();
		}
	}
}
