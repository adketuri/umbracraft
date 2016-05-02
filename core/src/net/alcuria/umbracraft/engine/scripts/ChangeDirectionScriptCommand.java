package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ChangeDirectionScriptCommand extends ScriptCommand {

	private static final int angles[] = { 0, 45, 90, 135, 180, 225, 270, 315, 360 };
	@Tooltip("If we want to only use the 4 cardinal directions")
	public boolean cardinal;
	@Tooltip("The direction to face, if fixed. Ignored if the target field is specified")
	public Direction direction = Direction.DOWN;
	@Tooltip("The entity we want to change the direction of")
	public String entity;
	@Tooltip("If not empty, the entity we want to face")
	public String target;

	@Override
	public ScriptCommand copy() {
		ChangeDirectionScriptCommand cmd = new ChangeDirectionScriptCommand();
		cmd.direction = direction;
		cmd.entity = entity;
		cmd.target = target;
		cmd.cardinal = cardinal;
		return cmd;
	}

	private String direction() {
		return StringUtils.isNotEmpty(target) ? target : (direction != null ? direction.toString() : "");
	}

	private String entity() {
		return StringUtils.isNotEmpty(entity) ? entity : "";
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Change Direction: " + entity() + ", " + direction();
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("target", Editor.db().entities().keys());
				put("entity", Editor.db().entities().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		Direction newDirection = null;
		Entity changingEntity = Game.entities().find(this.entity);
		if (changingEntity != null) {
			if (StringUtils.isNotEmpty(target)) {
				Entity targetEntity = Game.entities().find(target);
				if (targetEntity != null) {
					// calculate facing
					float dX = targetEntity.position.x - changingEntity.position.x;
					float dY = targetEntity.position.y - changingEntity.position.y;
					if (cardinal) {
						// for cardinal, we compare delta x/y to determine which is larger and get the subsequent direction from that
						if (Math.abs(dX) > Math.abs(dY)) {
							newDirection = dX > 0 ? Direction.RIGHT : Direction.LEFT;
						} else {
							newDirection = dY > 0 ? Direction.DOWN : Direction.RIGHT;
						}
					} else {
						// use atan to figure out the angle between two points and the horizontal
						// 0 degrees = west, 90 degrees = south, 180 degrees = east, 270 degrees = north
						float angle = (MathUtils.atan2(dY, dX)) * 180 / MathUtils.PI + 180;
						// get closest angle
						int closestAngle = 0;
						int closestValue = Integer.MAX_VALUE;
						for (int i = 0; i < angles.length; i++) {
							if (Math.abs(angle - angles[i]) < closestValue) {
								closestAngle = angles[i];
								closestValue = (int) Math.abs(angle - angles[i]);
							}
						}
						newDirection = Direction.from(closestAngle);
					}
				} else {
					// no entity found, print an error
					Game.error("No entity found with name: " + target);
				}
			} else {
				// no target, just change facing absolutely
				newDirection = direction;
			}
			// update the entity's direction
			// search for either a collection or a group, prioritizing the former
			AnimationCollectionComponent component = changingEntity.getComponent(AnimationCollectionComponent.class);
			if (component != null) {
				component.setDirection(newDirection);
			} else {
				AnimationGroupComponent group = changingEntity.getComponent(AnimationGroupComponent.class);
				if (group != null) {
					group.setDirection(newDirection);
				}
			}
		} else {
			Game.error("No entity found with name: " + target);
		}
		complete();
	}

	@Override
	public void update() {

	}

}
