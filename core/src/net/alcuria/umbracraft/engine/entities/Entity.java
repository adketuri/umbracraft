package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.engine.components.BaseComponent;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A top-level game object. Players, enemies, decorations, and so on, all should
 * be instantiated as GameObjects with logic separated out in the
 * {@link BaseComponent}.
 * @author Andrew Keturi */
public class Entity implements BaseEntity, Comparable<Entity> {

	// TODO: instead of passing a reference to the entity in our components, do something more sophisticated so all components don't have read/write access to these?
	private final Array<BaseComponent> components;
	public boolean onGround = true;
	public Vector3 position;
	public Vector3 velocity;

	/** Creates an entity with no components */
	public Entity() {
		components = new Array<BaseComponent>();
		position = new Vector3();
		velocity = new Vector3();
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

	@Override
	public int compareTo(Entity arg0) {
		return (int) (arg0.position.y - position.y);
	}

	/** Disposes/kills all components */
	public void dispose() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).dispose();
		}
	}

	/** Renders all components */
	@Override
	public void render() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).render(this);
		}
	}

	/** Updates all components
	 * @param delta time since last update */
	@Override
	public void update() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).update(this);
		}
	}
}
