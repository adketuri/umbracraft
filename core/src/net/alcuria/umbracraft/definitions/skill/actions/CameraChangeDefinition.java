package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.Vector3;

public class CameraChangeDefinition extends SkillActionDefinition {
	public static enum CameraDirection {
		CENTER(CENTER_POS, CENTER_POS), FAR(CENTER_POS - OFFSET, CENTER_POS + OFFSET), NEAR(CENTER_POS + OFFSET, CENTER_POS - OFFSET);

		private int friendly, nonFriendly;

		CameraDirection(int friendly, int nonFriendly) {
			this.friendly = friendly;
			this.nonFriendly = nonFriendly;
		}

		public int getPosition(final boolean isFriendly) {
			return isFriendly ? friendly : nonFriendly;
		}
	}

	private static final int CENTER_POS = 290; // center of the battle map
	private static final int OFFSET = 40; // offset for the left/right grid focus
	private final Entity cameraTarget;
	private final CameraDirection direction;
	private final boolean isFriendly;

	public CameraChangeDefinition(CameraDirection direction, boolean isFriendly, Entity cameraTarget) {
		this.direction = direction;
		this.cameraTarget = cameraTarget;
		this.isFriendly = isFriendly;
	}

	@Override
	public void start(Entity entity, Vector3 start, Vector3 target) {
		Game.view().setTarget(cameraTarget);
		cameraTarget.position.x = direction.getPosition(isFriendly);
	}

	@Override
	public void update(Entity entity, Listener stepCompleteListener) {
		stepCompleteListener.invoke();
	}

}
