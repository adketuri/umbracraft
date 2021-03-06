package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.KeyDownEvent;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.scripts.ConditionalCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand.CommandState;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A component for handling scripted events, such as cutscenes. Consists of a
 * {@link ScriptPageDefinition} that is read and updated accordingly.
 * @author Andrew Keturi */
public class ScriptComponent implements Component, EventListener {

	public static final String[] LOCAL_FLAGS = { "S1", "S2", "S3" };
	private boolean active = false;
	private final Rectangle collisionRect = new Rectangle();
	private final Array<ScriptCommand> commandStack = new Array<ScriptCommand>();
	private ScriptPageDefinition currentPage;
	private Entity entity;
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
		this.entity = entity;
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
		} else if (event instanceof FlagChangedEvent && entity != null && ((FlagChangedEvent) event).source != entity) {
			setCurrentPage(entity);
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
			String precondition = script.pages.get(i).precondition;
			for (String local : LOCAL_FLAGS) {
				if (StringUtils.isNotEmpty(precondition) && precondition.equals(local)) {
					precondition = precondition + entity.getId();
				}
			}
			if (precondition == null || !StringUtils.isNotEmpty(precondition) || Game.flags().isSet(precondition)) {
				currentPage = script.pages.get(i);
				break;
			}
		}

		// if the current page is the same as the last, we don't want to do anything
		if (currentPage == oldPage) {
			Game.debug("Page did not change, ignoring setCurrentPage for entity: " + entity.getName());
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

	/** Immediately marks the script as inactive. Careful with this. */
	public void setInactive() {
		active = false;
		Game.publisher().publish(new ScriptEndedEvent(currentPage));
	}

	/** Starts a script. should only be called once at the start */
	private void startScript() {
		if (currentPage.haltInput) {
			// halt player movement
			Game.publisher().publish(new ScriptStartedEvent(currentPage));
			final Entity player = Game.entities().find(Entity.PLAYER);
			if (player != null) {
				player.velocity.set(0, 0, 0);
			}
		}
		// add the page's command to the stack
		commandStack.clear();
		commandStack.add(currentPage.command);
		commandStack.get(0).setState(CommandState.NOT_STARTED);
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
				collisionRect.set(entity.position.x - collision.getWidth() / 2, entity.position.y - collision.getHeight() / 2, collision.getWidth(), collision.getHeight());
				return source != null && collisionRect.contains(source.x, source.y);
			}
		}
		return false;
	}

	@Override
	public void update(Entity entity) {
		// if we have some pages to execute, see if we can do that
		if (currentPage == null) {
			return;
		}
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
		// ensure we have commands to execute
		if (commandStack.size > 0) {
			final ScriptCommand current = commandStack.get(commandStack.size - 1);
			// get a reference to the top of the stack
			if (current != null) {
				switch (current.getState()) {
				case COMPLETE:
					Game.debug("Completing " + current.getName());
					final ScriptCommand next = current.getNext();
					if (next != null) {
						next.setState(CommandState.NOT_STARTED);
					}
					commandStack.set(commandStack.size - 1, next);
					Game.debug("  inserted, new size is " + commandStack.size);
					Game.debug("  Next is " + (next != null ? next.getName() : "null"));
					if (current instanceof ConditionalCommand) {
						final ConditionalCommand cond = (ConditionalCommand) current;
						if (cond.isNextNested()) {
							// we're inside a block so let's push it to the command stack
							commandStack.add(cond.getCalculated());
							cond.getCalculated().setState(CommandState.NOT_STARTED);
							Game.debug("    Inserting for cond: " + (cond.getCalculated().getName()));
							Game.debug("    new size is " + commandStack.size);
						}

					}
					break;
				case NOT_STARTED:
					Game.debug("Starting " + current.getName());
					current.start(entity);
					break;
				case STARTED:
					current.update();
					break;
				default:
					break;
				}
			}
		}
		// check if we're done with all scripts. stack size changes above so we can't dump this in the else.
		if (commandStack.size < 1) {
			setInactive();
			setCurrentPage(entity);
		} else if (commandStack.get(commandStack.size - 1) == null) {
			commandStack.pop();
		}
	}
}
