package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.engine.components.Component;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A top-level game object. Players, enemies, decorations, and so on, all should
 * be instantiated as GameObjects with logic separated out in the
 * {@link Component}.
 * @author Andrew Keturi */
public class Entity implements BaseEntity, Comparable<Entity> {

	public static final String PLAYER = "Player";

	// TODO: instead of passing a reference to the entity in our components, do something more sophisticated so all components don't have read/write access to these?
	private final Array<Component> components;
	private String name;
	public boolean onGround = true;
	public Vector3 position;
	public Vector3 velocity;

	/** Creates an entity with no components */
	public Entity() {
		components = new Array<Component>();
		position = new Vector3();
		velocity = new Vector3();
	}

	/** Creates an entity with several pre-defined components
	 * @param components the initial components */
	public Entity(Component... components) {
		this();
		for (Component component : components) {
			component.create(this);
			this.components.add(component);
		}
	}

	/** Adds a single component after instantiation
	 * @param component the component to add */
	public void addComponent(Component component) {
		component.create(this);
		components.add(component);
	}

	/** Adds a component from a definition
	 * @param definition */
	public void addComponent(ComponentDefinition definition) {
		addComponent(definition.create());
	}

	@Override
	public int compareTo(Entity arg0) {
		return (int) (arg0.position.y - position.y);
	}

	/** Disposes/kills all components */
	public void dispose() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).dispose(this);
		}
	}

	/** Gets a component
	 * @param clazz the component type
	 * @return the component */
	public <T extends Component> T getComponent(Class<T> clazz) {
		for (int i = 0; i < components.size; i++) {
			if (clazz.isInstance(components.get(i))) {
				return (T) components.get(i);
			}
		}
		return null;
	}

	/** @return the name */
	public String getName() {
		return name;
	}

	/** Removes a component
	 * @param clazz the component type */
	public void removeComponent(Class<? extends Component> clazz) {
		for (Component component : components) {
			if (clazz.isInstance(component)) {
				components.removeValue(component, true);
				return;
			}
		}
	}

	/** Renders all components */
	@Override
	public void render() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).render(this);
		}
	}

	/** @param name the name to set */
	public void setName(String name) {
		this.name = name;
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
