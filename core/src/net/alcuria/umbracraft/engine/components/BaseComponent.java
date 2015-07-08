package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.entities.Entity;

/** A basic component
 * @author Andrew Keturi */
public interface BaseComponent {

	/** To be called to do any initialization needed for the component */
	public void create(Entity entity);

	/** To be called when the component needs to be destroyed */
	public void dispose(Entity entity);

	/** To be called when the component needs to be rendered */
	public void render(Entity entity);

	/** To be called when the component needs to be updated */
	public void update(Entity entity);
}
