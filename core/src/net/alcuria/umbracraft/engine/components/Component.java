package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.entities.Entity;

/** The interface for all components.
 * @author Andrew Keturi */
public interface Component {

	/** To be called to do any initialization needed for the component */
	void create(Entity entity);

	/** To be called when the component needs to be destroyed */
	void dispose(Entity entity);

	/** To be called when the component needs to be rendered */
	void render(Entity entity);

	/** To be called when the component needs to be updated */
	void update(Entity entity);
}
