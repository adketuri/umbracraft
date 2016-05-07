package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.IgnorePopulate;
import net.alcuria.umbracraft.annotations.Tooltip;

/** Contains constants to define a {@link SkillActionDefinition} that moves the
 * camera.
 * @author Andrew Keturi */
public class CameraChangeActionDefinition extends SkillActionDefinition {

	public static enum CameraDirection {
		CENTER(CENTER_POS, CENTER_POS), FAR(CENTER_POS - OFFSET, CENTER_POS + OFFSET), NEAR(CENTER_POS + OFFSET, CENTER_POS - OFFSET);

		private final int friendly, nonFriendly;

		CameraDirection(int friendly, int nonFriendly) {
			this.friendly = friendly;
			this.nonFriendly = nonFriendly;
		}

		public int getPosition(final boolean isFriendly) {
			return isFriendly ? friendly : nonFriendly;
		}
	}

	@IgnorePopulate
	private static final int CENTER_POS = 290; // center of the battle map
	@IgnorePopulate
	private static final int OFFSET = 40; // offset for the left/right grid focus
	@Tooltip("The position of the camera on the field")
	public CameraDirection position;
}
