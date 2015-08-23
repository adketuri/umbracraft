package net.alcuria.umbracraft.definitions.component;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.ControlledInputComponent;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.components.EntityCollisionComponent;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;
import net.alcuria.umbracraft.engine.components.ScriptComponent;
import net.alcuria.umbracraft.engine.entities.ShadowComponent;

/** Defines a simple component.
 * @author Andrew Keturi */
public class ComponentDefinition extends Definition {

	/** Defines the {@link AnimationCollectionComponent} for an entity.
	 * @author Andrew Keturi */
	public static class AnimationCollectionComponentDefinition extends ComponentDefinition {
		/** The AnimationCollectionComponent to use */
		public String animationCollectionComponent;
	}

	/** Defines the {@link AnimationComponent} for an entity.
	 * @author Andrew Keturi */
	public static class AnimationComponentDefinition extends ComponentDefinition {
		/** The AnimationComponent to use */
		public String animationComponent;
	}

	/** An enumeration of all component type definitions.
	 * @author Andrew Keturi */
	public static enum ComponentType {
		ANIM(AnimationComponentDefinition.class), //
		ANIM_COLLECTION(AnimationCollectionComponentDefinition.class), //
		CONTROLLED_INPUT(ControlledInputComponentDefinition.class), //
		DIRECTED_INPUT(DirectedInputComponentDefinition.class), //
		ENTITY_COLLISION(EntityCollisionComponentDefinition.class), //
		MAP_COLLISION(MapCollisionComponentDefinition.class), //
		SCRIPT(ScriptComponentDefinition.class), //
		SHADOW(ShadowComponentDefinition.class);

		/** The component type's corresponding {@link ComponentDefinition} */
		public final Class<? extends ComponentDefinition> clazz;

		private ComponentType(Class<? extends ComponentDefinition> clazz) {
			this.clazz = clazz;
		}
	}

	/** Defines the {@link ControlledInputComponent} for an entity. */
	public static class ControlledInputComponentDefinition extends ComponentDefinition {
	}

	/** Defines the {@link DirectedInputComponent} for an entity. */
	public static class DirectedInputComponentDefinition extends ComponentDefinition {
	}

	/** Defines the {@link EntityCollisionComponent} for an entity. */
	public static class EntityCollisionComponentDefinition extends ComponentDefinition {
	}

	/** Defines the {@link MapCollisionComponent} for an entity. */
	public static class MapCollisionComponentDefinition extends ComponentDefinition {
		/** The height of the collision component */
		public int height;
		/** The width of the collision component */
		public int width;
	}

	/** Defines the {@link ScriptComponent} for an entity. */
	public static class ScriptComponentDefinition extends ComponentDefinition {
	}

	/** Defines the {@link ShadowComponent} for an entity. */
	public static class ShadowComponentDefinition extends ComponentDefinition {
	}

	/** The component name */
	public String name;

	@Override
	public String getName() {
		return name != null ? name : "Component";
	}
}
