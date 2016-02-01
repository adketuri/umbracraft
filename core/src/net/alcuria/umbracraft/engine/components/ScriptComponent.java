package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand.CommandState;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/** A component for handling scripted events, such as cutscenes. Consists of a
 * {@link ScriptPageDefinition} that is read and updated accordingly.
 * @author Andrew Keturi */
public class ScriptComponent implements Component, EventListener {

	private boolean active = false;
	private final Rectangle collisionRect = new Rectangle();
	private ScriptCommand currentCommand;
	private ScriptPageDefinition currentPage;
	private boolean pressed = false, collided = false;
	private ScriptDefinition script;
	private final Vector3 source = new Vector3();

	public ScriptComponent(String scriptId) {
		if (scriptId != null) {
			script = Game.db().script(scriptId);
		} else {
			Game.error("Script ID is null. Cannot attach component.");
		}
	}

	@Override
	public void create(final Entity entity) {
		setCurrentPage(entity);
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose(Entity entity) {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof KeyDownEvent) {
			pressed = true;
			source.set(((KeyDownEvent) event).source);
		}
	}

	@Override
	public void render(Entity entity) {

	}

	/** Call when some other entity collides with this entity */
	public void setCollided() {
		collided = true;
	}

	private void setCurrentPage(Entity entity) {
		if (script == null) {
			return;
		}
		ScriptPageDefinition oldPage = currentPage;
		// go thru the pages in reverse, finding the first page that has its preconditions met
		for (int i = script.pages.size - 1; i >= 0; i--) {
			final String precondition = script.pages.get(i).precondition;
			if (precondition == null || !StringUtils.isNotEmpty(precondition) || Game.flags().isSet(precondition)) {
				currentPage = script.pages.get(i);
				break;
			}
		}

		// if the current page is the same as the last, we don't want to do anything
		if (currentPage == oldPage) {
			Game.log("Page did not change, ignoring setCurrentPage for entity: " + entity.getName());
			return;
		}

		// update the animation/animationGroup components
		if (currentPage != null && (currentPage.animationCollection != null || currentPage.animationGroup != null || currentPage.animation != null)) {
			entity.removeComponent(AnimationComponent.class);
			entity.removeComponent(AnimationGroupComponent.class);
			entity.removeComponent(AnimationCollectionComponent.class);
			if (StringUtils.isNotEmpty(currentPage.animationCollection)) {
				entity.addComponent(new AnimationCollectionComponent(Game.db().animCollection(currentPage.animationCollection)));
			} else if (StringUtils.isNotEmpty(currentPage.animationGroup)) {
				entity.addComponent(new AnimationGroupComponent(Game.db().animGroup(currentPage.animationGroup)));
			} else if (StringUtils.isNotEmpty(currentPage.animation)) {
				entity.addComponent(new AnimationComponent(Game.db().anim(currentPage.animation)));
			}
		}
	}

	/** Starts a script. should only be called once at the start */
	private void startScript() {
		if (currentPage.haltInput) {
			// halt player movement
			Game.publisher().publish(new ScriptStartedEvent(currentPage));
			Game.entities().find(Entity.PLAYER).velocity.set(0, 0, 0);
		}
		currentCommand = currentPage.command;
		active = true;
		pressed = false;
	}

	/** Checks if two entities are in close proximity, using the source vector.
	 * @param entity the {@link Entity}
	 * @return true if the vector is close */
	private boolean touching(Entity entity) {
		pressed = false;
		if (MathUtils.isEqual(source.z, entity.position.z)) {
			MapCollisionComponent collision = entity.getComponent(MapCollisionComponent.class);
			if (collision != null) {
				collisionRect.set(entity.position.x, entity.position.y, collision.getWidth(), collision.getHeight());
				return source != null && collisionRect.contains(source.x, source.y);
			}
		}
		return false;
	}

	@Override
	public void update(Entity entity) {
		// if we have some pages to execute, see if we can do that
		if (!active) {
			switch (currentPage.trigger) {
			case INSTANT:
				startScript();
				break;
			case ON_INTERACTION:
				if (pressed && touching(entity)) {
					startScript();
				}
				break;
			case ON_TOUCH:
				if (collided) {
					startScript();
					collided = false;
				}
			default:

			}
		} else {
			collided = false;
			updateScript(entity);
		}

	}

	/** Updates the script, assumes all preconditions are met. (Eg., key has been
	 * pressed, etc.) */
	private void updateScript(Entity entity) {
		// if its done, increment our index
		switch (currentCommand.getState()) {
		case COMPLETE:
			currentCommand.setState(CommandState.NOT_STARTED);
			currentCommand = currentCommand.getNext();
			break;
		case NOT_STARTED:
			currentCommand.start(entity);
			break;
		case STARTED:
			currentCommand.update();
			break;
		default:
			break;
		}
		// check if we're done with all scripts
		if (currentCommand == null) {
			setCurrentPage(entity);
			active = false;
			Game.publisher().publish(new ScriptEndedEvent(currentPage));
		}
	}
}
