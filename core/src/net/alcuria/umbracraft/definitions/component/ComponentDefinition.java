package net.alcuria.umbracraft.definitions.component;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.Component;
import net.alcuria.umbracraft.engine.components.ControlledInputComponent;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.components.EntityCollisionComponent;
import net.alcuria.umbracraft.engine.components.IndicatorComponent;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;
import net.alcuria.umbracraft.engine.components.PlatformComponent;
import net.alcuria.umbracraft.engine.components.ScriptComponent;
import net.alcuria.umbracraft.engine.entities.ShadowComponent;

/** Defines a simple component.
 * @author Andrew Keturi */
public abstract class ComponentDefinition extends Definition {

	/** Defines the {@link AnimationCollectionComponent} for an entity.
	 * @author Andrew Keturi */
	public static class AnimationCollectionComponentDefinition extends ComponentDefinition {
		/** The AnimationCollectionComponent to use */
		public String animationCollectionComponent;

		@Override
		public Component create() {
			return new AnimationCollectionComponent(Game.db().animCollection(animationCollectionComponent));
		}
	}

	/** Defines the {@link AnimationComponent} for an entity.
	 * @author Andrew Keturi */
	public static class AnimationComponentDefinition extends ComponentDefinition {
		/** The AnimationComponent to use */
		public String animationComponent;

		@Override
		public Component create() {
			return new AnimationComponent(Game.db().anim(animationComponent));
		}
	}

	/** An enumeration of all component type definitions.
	 * @author Andrew Keturi */
	public static enum ComponentType {
		ANIM(AnimationComponentDefinition.class), //
		ANIM_COLLECTION(AnimationCollectionComponentDefinition.class), //
		CONTROLLED_INPUT(ControlledInputComponentDefinition.class), //
		DIRECTED_INPUT(DirectedInputComponentDefinition.class), //
		ENTITY_COLLISION(EntityCollisionComponentDefinition.class), //
		INDICATOR(IndicatorOperationComponentDefinition.class), //
		MAP_COLLISION(MapCollisionComponentDefinition.class), //
		PLATFORM(PlatformComponentDefinition.class), //
		SCRIPT(ScriptComponentDefinition.class), //
		SHADOW(ShadowComponentDefinition.class);

		/** The component type's corresponding {@link ComponentDefinition} */
		public final Class<? extends ComponentDefinition> clazz;

		private ComponentType(Class<? extends ComponentDefinition> clazz) {
			this.clazz = clazz;
		}

		@Override
		public String toString() {
			switch (this) {
			case ANIM:
				return "Animation";
			case ANIM_COLLECTION:
				return "Animation Collection";
			case CONTROLLED_INPUT:
				return "Controlled Input";
			case DIRECTED_INPUT:
				return "Directed Input";
			case ENTITY_COLLISION:
				return "Entity Collision";
			case MAP_COLLISION:
				return "Map Collision";
			case SCRIPT:
				return "Script";
			case SHADOW:
				return "Shadow";
			case INDICATOR:
				return "Indicator";
			case PLATFORM:
				return "Platform";
			default:
				return "Unknown";
			}
		}
	}

	/** Defines the {@link ControlledInputComponent} for an entity. */
	public static class ControlledInputComponentDefinition extends ComponentDefinition {

		@Override
		public Component create() {
			return new ControlledInputComponent();
		}
	}

	/** Defines the {@link DirectedInputComponent} for an entity. */
	public static class DirectedInputComponentDefinition extends ComponentDefinition {

		@Override
		public Component create() {
			return new DirectedInputComponent();
		}
	}

	/** Defines the {@link EntityCollisionComponent} for an entity. */
	public static class EntityCollisionComponentDefinition extends ComponentDefinition {

		@Override
		public Component create() {
			return new EntityCollisionComponent();
		}
	}

	/** Defines the {@link ScriptComponent} for an entity. */
	public static class IndicatorOperationComponentDefinition extends ComponentDefinition {

		@Override
		public Component create() {
			return new IndicatorComponent();
		}
	}

	/** Defines the {@link MapCollisionComponent} for an entity. */
	public static class MapCollisionComponentDefinition extends ComponentDefinition {
		/** The height of the collision component */
		public int height;
		/** The width of the collision component */
		public int width;

		@Override
		public Component create() {
			return new MapCollisionComponent(width, height);
		}
	}

	/** Defines the {@link PlatformComponent} for an entity. */
	public static class PlatformComponentDefinition extends ComponentDefinition {

		@Override
		public Component create() {
			return new PlatformComponent();
		}
	}

	/** Defines the {@link ScriptComponent} for an entity. */
	public static class ScriptComponentDefinition extends ComponentDefinition {
		/** The name of the script */
		public String script;

		@Override
		public Component create() {
			return new ScriptComponent(script);
		}
	}

	/** Defines the {@link ShadowComponent} for an entity. */
	public static class ShadowComponentDefinition extends ComponentDefinition {

		public boolean squareShadow;

		@Override
		public Component create() {
			return new ShadowComponent(squareShadow);
		}
	}

	/** The component name */
	public String name;

	public abstract Component create();

	@Override
	public String getName() {
		return name != null ? name : "Component";
	}

	@Override
	public String getTag() {
		return "";
	}
}
