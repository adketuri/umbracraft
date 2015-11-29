package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

/** Contains constants to define a {@link SkillActionDefinition} that moves the
 * camera.
 * @author Andrew Keturi */
public class CameraChangeActionDefinition extends SkillActionDefinition {
	public enum CameraPosition {
		CENTER, FAR, NEAR
	}

	@Tooltip("The position of the camera on the field")
	public CameraPosition position;
}
