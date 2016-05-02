package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.engine.components.Component;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A top-level game object. Players, enemies, decorations, and so on, all should
 * be instantiated as Entities with logic separated out in the {@link Component}
 * objects.
 * @author Andrew Keturi */
public class Entity implements BaseEntity, Comparable<Entity> {

	public static final String PLAYER = "Player";
	private final Array<String> args = new Array<String>();
	private final Array<Component> components;
	private String name, tag;
	public Vector3 position, velocity;

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

	/** Creates an entity with no components and a given name
	 * @param name the name {@link String} */
	public Entity(String name) {
		this();
		this.name = name;
	}

	/** Creates an entity with no components and a given name + tag
	 * @param name the name {@link String}
	 * @param tag the tag {@link String} */
	public Entity(String name, String tag) {
		this();
		this.name = name;
		this.tag = tag;
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

	/** @return arguments passed into the entity for creation from the map editor */
	public Array<String> getArguments() {
		return args;
	}

	/** Gets a component. Returns <code>null</code> if the component is not
	 * present. Be sure to check the return value before making assumptions.
	 * @param clazz the {@link Component} type
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

	/** @return the tag */
	public String getTag() {
		return tag;
	}

	/** A helper function to determine if this Entity is tagged with a particular
	 * {@link String}. A match is returned if and only if the tags are both non
	 * null and match (via String comparison).
	 * @param tag a {@link String} to check
	 * @return <code>true</code> if the entity has this tag */
	public boolean isTagged(String tag) {
		return tag != null && this.tag != null && this.tag.equals(tag);
	}

	/** Removes a component from the entity
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

	/** Given an {@link EntityReferenceDefinition}, sets the relevant fields of
	 * the entity.
	 * @param reference an {@link EntityReferenceDefinition} to use when
	 *        updating this entity. */
	public void setFromReference(EntityReferenceDefinition reference) {
		setName(reference.name);
		position.x = reference.x * Config.tileWidth;
		position.y = reference.y * Config.tileWidth;
		args.clear();
		args.addAll(reference.arg1, reference.arg2, reference.arg3);
	}

	/** @param name the name to set */
	public void setName(String name) {
		this.name = name;
	}

	/** Sets the position vector equal to the value of another vector
	 * @param position a {@link Vector3} */
	public void setPosition(Vector3 position) {
		if (position == null) {
			Game.error("position is null");
			return;
		}
		this.position.x = position.x;
		this.position.y = position.y;
	}

	/** @param tag the tag to set */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/** Updates all components */
	@Override
	public void update() {
		for (int i = 0; i < components.size; i++) {
			components.get(i).update(this);
		}
	}
}
