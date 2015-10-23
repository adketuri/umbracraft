package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.Vector3;

/** A step in a skill's animation
 * @author Andrew Keturi */
public abstract class SkillActionDefinition {

	public abstract void start(Entity entity, Vector3 start, Vector3 target);

	/** Updates the entity. TODO: should we have this elsewhere and keep the
	 * definition purely for data?
	 * @param entity the entity to update
	 * @param target coordinates of the skill's target cell
	 * @param stepCompleteListener called when a step is done */
	public abstract void update(Entity entity, Listener stepCompleteListener);

}
