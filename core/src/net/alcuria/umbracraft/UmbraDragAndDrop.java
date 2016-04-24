package net.alcuria.umbracraft;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

/** Manages drag and drop operations through registered drag sources and drop
 * targets.
 * @author Nathan Sweet */
public class UmbraDragAndDrop {
	/** The payload of a drag and drop operation. Actors can be optionally
	 * provided to follow the cursor and change when over a target. */
	static public class Payload {
		Actor dragActor, validDragActor, invalidDragActor;
		Object object;

		public Actor getDragActor() {
			return dragActor;
		}

		public Actor getInvalidDragActor() {
			return invalidDragActor;
		}

		public Object getObject() {
			return object;
		}

		public Actor getValidDragActor() {
			return validDragActor;
		}

		public void setDragActor(Actor dragActor) {
			this.dragActor = dragActor;
		}

		public void setInvalidDragActor(Actor invalidDragActor) {
			this.invalidDragActor = invalidDragActor;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public void setValidDragActor(Actor validDragActor) {
			this.validDragActor = validDragActor;
		}
	}

	/** A target where a payload can be dragged from.
	 * @author Nathan Sweet */
	static abstract public class Source {
		final Actor actor;

		public Source(Actor actor) {
			if (actor == null) {
				throw new IllegalArgumentException("actor cannot be null.");
			}
			this.actor = actor;
		}

		/** @return May be null. */
		abstract public Payload dragStart(InputEvent event, float x, float y, int pointer);

		/** @param payload null if dragStart returned null.
		 * @param target null if not dropped on a valid target. */
		public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
		}

		public Actor getActor() {
			return actor;
		}
	}

	/** A target where a payload can be dropped to.
	 * @author Nathan Sweet */
	static abstract public class Target {
		final Actor actor;

		public Target(Actor actor) {
			if (actor == null) {
				throw new IllegalArgumentException("actor cannot be null.");
			}
			this.actor = actor;
			Stage stage = actor.getStage();
			if (stage != null && actor == stage.getRoot()) {
				throw new IllegalArgumentException("The stage root cannot be a drag and drop target.");
			}
		}

		/** Called when the object is dragged over the target. The coordinates
		 * are in the target's local coordinate system.
		 * @return true if this is a valid target for the object. */
		abstract public boolean drag(Source source, Payload payload, float x, float y, int pointer);

		abstract public void drop(Source source, Payload payload, float x, float y, int pointer);

		public Actor getActor() {
			return actor;
		}

		/** Called when the object is no longer over the target, whether because
		 * the touch was moved or a drop occurred. */
		public void reset(Source source, Payload payload) {
		}
	}

	static final Vector2 tmpVector = new Vector2();
	int activePointer = -1;
	private int button;
	boolean cancelTouchFocus = true;
	Actor dragActor;
	float dragActorX = 14, dragActorY = -20;
	long dragStartTime;
	int dragTime = 250;
	boolean isValidTarget;
	boolean keepWithinStage = true;
	Payload payload;
	ObjectMap<Source, DragListener> sourceListeners = new ObjectMap();
	private float tapSquareSize = 8;

	Target target;

	Array<Target> targets = new Array();

	float touchOffsetX, touchOffsetY;

	public void addSource(final Source source) {
		DragListener listener = new DragListener() {
			@Override
			public void drag(InputEvent event, float x, float y, int pointer) {
				if (payload == null) {
					return;
				}
				if (pointer != activePointer) {
					return;
				}

				Stage stage = event.getStage();

				Touchable dragActorTouchable = null;
				if (dragActor != null) {
					dragActorTouchable = dragActor.getTouchable();
					dragActor.setTouchable(Touchable.disabled);
				}

				// Find target.
				Target newTarget = null;
				isValidTarget = false;
				float stageX = event.getStageX() + touchOffsetX, stageY = event.getStageY() + touchOffsetY;
				Actor hit = event.getStage().hit(stageX, stageY, true); // Prefer touchable actors.
				if (hit == null) {
					hit = event.getStage().hit(stageX, stageY, false);
				}
				if (hit != null) {
					for (int i = 0, n = targets.size; i < n; i++) {
						Target target = targets.get(i);
						if (!target.actor.isAscendantOf(hit)) {
							continue;
						}
						newTarget = target;
						target.actor.stageToLocalCoordinates(tmpVector.set(stageX, stageY));
						break;
					}
				}
				//if over a new target, notify the former target that it's being left behind.
				if (newTarget != target) {
					if (target != null) {
						target.reset(source, payload);
					}
					target = newTarget;
				}
				//with any reset out of the way, notify new targets of drag.
				if (newTarget != null) {
					isValidTarget = newTarget.drag(source, payload, tmpVector.x, tmpVector.y, pointer);
				}

				if (dragActor != null) {
					dragActor.setTouchable(dragActorTouchable);
				}

				// Add/remove and position the drag actor.
				Actor actor = null;
				if (target != null) {
					actor = isValidTarget ? payload.validDragActor : payload.invalidDragActor;
				}
				if (actor == null) {
					actor = payload.dragActor;
				}
				if (actor == null) {
					return;
				}
				if (dragActor != actor) {
					if (dragActor != null) {
						dragActor.remove();
					}
					dragActor = actor;
					stage.addActor(actor);
				}
				float actorX = event.getStageX() + dragActorX;
				float actorY = event.getStageY() + dragActorY - actor.getHeight();
				if (keepWithinStage) {
					if (actorX < 0) {
						actorX = 0;
					}
					if (actorY < 0) {
						actorY = 0;
					}
					if (actorX + actor.getWidth() > stage.getWidth()) {
						actorX = stage.getWidth() - actor.getWidth();
					}
					if (actorY + actor.getHeight() > stage.getHeight()) {
						actorY = stage.getHeight() - actor.getHeight();
					}
				}
				actor.setPosition(actorX, actorY);
			}

			@Override
			public void dragStart(InputEvent event, float x, float y, int pointer) {
				if (activePointer != -1) {
					event.stop();
					return;
				}

				activePointer = pointer;

				dragStartTime = System.currentTimeMillis();
				payload = source.dragStart(event, getTouchDownX(), getTouchDownY(), pointer);
				event.stop();

				if (cancelTouchFocus && payload != null) {
					source.getActor().getStage().cancelTouchFocusExcept(this, source.getActor());
				}
			}

			@Override
			public void dragStop(InputEvent event, float x, float y, int pointer) {
				if (pointer != activePointer) {
					return;
				}
				activePointer = -1;
				if (payload == null) {
					return;
				}

				if (System.currentTimeMillis() - dragStartTime < dragTime) {
					isValidTarget = false;
				}
				if (dragActor != null) {
					dragActor.remove();
				}
				if (isValidTarget) {
					float stageX = event.getStageX() + touchOffsetX, stageY = event.getStageY() + touchOffsetY;
					target.actor.stageToLocalCoordinates(tmpVector.set(stageX, stageY));
					target.drop(source, payload, tmpVector.x, tmpVector.y, pointer);
				}
				source.dragStop(event, x, y, pointer, payload, isValidTarget ? target : null);
				if (target != null) {
					target.reset(source, payload);
				}
				payload = null;
				target = null;
				isValidTarget = false;
				dragActor = null;
			}
		};
		listener.setTapSquareSize(tapSquareSize);
		listener.setButton(button);
		source.actor.addCaptureListener(listener);
		sourceListeners.put(source, listener);
	}

	public void addTarget(Target target) {
		targets.add(target);
	}

	/** Removes all targets and sources. */
	public void clear() {
		targets.clear();
		for (Entry<Source, DragListener> entry : sourceListeners.entries()) {
			entry.key.actor.removeCaptureListener(entry.value);
		}
		sourceListeners.clear();
	}

	/** Returns the current drag actor, or null. */
	public Actor getDragActor() {
		return dragActor;
	}

	public boolean isDragging() {
		return payload != null;
	}

	public void removeSource(Source source) {
		DragListener dragListener = sourceListeners.remove(source);
		source.actor.removeCaptureListener(dragListener);
	}

	public void removeTarget(Target target) {
		targets.removeValue(target, true);
	}

	/** Sets the button to listen for, all other buttons are ignored. Default is
	 * {@link Buttons#LEFT}. Use -1 for any button. */
	public void setButton(int button) {
		this.button = button;
	}

	/** When true (default), the {@link Stage#cancelTouchFocus()} touch focus} is
	 * cancelled if {@link Source#dragStart(InputEvent, float, float, int)
	 * dragStart} returns non-null. This ensures the DragAndDrop is the only
	 * touch focus listener, eg when the source is inside a {@link ScrollPane}
	 * with flick scroll enabled. */
	public void setCancelTouchFocus(boolean cancelTouchFocus) {
		this.cancelTouchFocus = cancelTouchFocus;
	}

	public void setDragActorPosition(float dragActorX, float dragActorY) {
		this.dragActorX = dragActorX;
		this.dragActorY = dragActorY;
	}

	/** Time in milliseconds that a drag must take before a drop will be
	 * considered valid. This ignores an accidental drag and drop that was meant
	 * to be a click. Default is 250. */
	public void setDragTime(int dragMillis) {
		dragTime = dragMillis;
	}

	public void setKeepWithinStage(boolean keepWithinStage) {
		this.keepWithinStage = keepWithinStage;
	}

	/** Sets the distance a touch must travel before being considered a drag. */
	public void setTapSquareSize(float halfTapSquareSize) {
		tapSquareSize = halfTapSquareSize;
	}

	/** Sets an offset in stage coordinates from the touch position which is used
	 * to determine the drop location. Default is 0,0. */
	public void setTouchOffset(float touchOffsetX, float touchOffsetY) {
		this.touchOffsetX = touchOffsetX;
		this.touchOffsetY = touchOffsetY;
	}
}
