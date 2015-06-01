package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.engine.components.BaseComponent;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A top-level game object. Players, enemies, decorations, and so on, all should
 * be instantiated as GameObjects with logic separated out in the
 * {@link BaseComponent}.
 * @author Andrew Keturi */
public class Entity {

	//public int altitude;
	private final Array<BaseComponent> components;
	public Vector3 desiredPosition;
	public Vector3 position;
	public Vector3 velocity;

	/** Creates an entity with no components */
	public Entity() {
		components = new Array<BaseComponent>();
		position = new Vector3();
		velocity = new Vector3();
		desiredPosition = new Vector3();
	}

	/** Creates an entity with several pre-defined components
	 * @param components the initial components */
	public Entity(BaseComponent... components) {
		this();
		for (BaseComponent component : components) {
			component.create();
			this.components.add(component);
		}
	}

	/** Adds a single component after instantiation
	 * @param component the component to add */
	public void addComponent(BaseComponent component) {
		component.create();
		components.add(component);
	}

	/** Disposes/kills all components */
	public void dispose() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).dispose();
		}
	}

	/** Renders all components */
	public void render() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).render(this);
		}
	}

	/** Updates all components
	 * @param delta time since last update */
	public void update(float delta) {
		for (int i = 0; i < components.size; i++) {
			components.get(i).update(this);
		}
	}
}
