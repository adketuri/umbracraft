package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.util.StringUtils;

/** Defines an action for the game's battle system. No logic is contained within
 * the definition.
 * @author Andrew Keturi */
public abstract class SkillActionDefinition extends Definition {

	public static enum SkillActionType {
		APPROACH(ApproachSkillActionDefinition.class), //
		CAMERA(CameraChangeActionDefinition.class), //
		DAMAGE(DamageSkillActionDefinition.class), //
		GRID_EFFECT(PlaceGridEffectActionDefinition.class), //
		POSE_CHANGE(PoseChangeActionDefinition.class), //
		RETURN(ReturnSkillActionDefinition.class), //
		SHAKE(ShakeScreenSkillActionDefinition.class), //
		SOUND(PlaySoundSkillActionDefinition.class), //
		TIMED(TimedHitSkillActionDefinition.class), //
		WAIT(WaitSkillActionDefinition.class);

		/** The component type's corresponding {@link ComponentDefinition} */
		public final Class<? extends SkillActionDefinition> clazz;

		private SkillActionType(Class<? extends SkillActionDefinition> clazz) {
			this.clazz = clazz;
		}

		@Override
		public String toString() {
			return StringUtils.splitCamelCase(clazz.getSimpleName().replace("SkillActionDefinition", "").replace("ActionDefinition", ""));
		}
	}

	@Override
	public String getName() {
		return "Action";
	}

	@Override
	public String getTag() {
		return "";
	}
}
