package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent.Pose;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** A component to display a single animation.
 * @author Andrew Keturi */
public class AnimationComponent implements Component {

	//private float alpha = 1;
	private Listener completeListener;
	private int curFrameCount, curFrameIndex;
	private AnimationDefinition definition;
	private final Direction direction;
	private TypeListener<String> frameChangedListener;	// invoked when a frame changes
	private Array<TextureRegion> frames;
	private boolean mirrorAll;
	private final Vector2 origin = new Vector2();
	private boolean played = false;
	private final String template;
	private Pose templatePose;
	private int templateX, templateY;

	public AnimationComponent(AnimationDefinition definition) {
		this.definition = definition;
		template = null;
		direction = null;
	}

	public AnimationComponent(String template, int x, int y, Pose pose, Direction direction, AnimationDefinition definition) {
		this.definition = definition;
		this.template = template;
		this.direction = direction;
		templateX = x;
		templateY = y;
		templatePose = pose;
	}

	@Override
	public void create(Entity entity) {
		// see if we need to template this. if so create definition
		if (StringUtils.isNotEmpty(template)) {
			//TODO: at some point config this correctly
			definition = new AnimationDefinition();
			definition.width = 24;
			definition.height = 32;
			definition.originX = 24 / 2;
			definition.originY = 2;
			definition.frames = new Array<AnimationFrameDefinition>();
			definition.filename = template;
			switch (templatePose) {
			case WALKING:
				int idx[] = { 0, 1, 2, 1 };
				for (int i = 0; i < idx.length; i++) {
					AnimationFrameDefinition walkFrameDef = new AnimationFrameDefinition();
					walkFrameDef.duration = 5;
					walkFrameDef.x = templateX + idx[i];
					walkFrameDef.y = templateX + direction.getTemplateIndex();
					definition.frames.add(walkFrameDef);
				}

				break;
			default:
				AnimationFrameDefinition idleFrameDef = new AnimationFrameDefinition();
				idleFrameDef.duration = 8;
				idleFrameDef.x = 1;
				idleFrameDef.y = direction.getTemplateIndex();
				definition.frames.add(idleFrameDef);
				break;
			}
		}

		// now create
		frames = new Array<TextureRegion>();
		curFrameCount = curFrameIndex = 0;
		if (definition != null) {
			// definition-based animation
			final Texture texture = Game.assets().get("sprites/animations/" + definition.filename + ".png", Texture.class);
			for (AnimationFrameDefinition frame : definition.frames) {
				frames.add(new TextureRegion(texture, frame.x * definition.width, frame.y * definition.height, definition.width, definition.height));
			}
			origin.x = definition.originX;
			origin.y = definition.originY;
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	/** @return the {@link AnimationFrameDefinition} of the current frame playing */
	public AnimationFrameDefinition getFrameDefinition() {
		if (definition == null || definition.frames == null) {
			throw new NullPointerException("Frames are null");
		}
		return definition.frames.get(curFrameIndex);
	}

	@Override
	public void render(Entity entity) {
		if (frames != null && frames.size > 0) {
			final boolean mirror = mirrorAll ? !definition.frames.get(curFrameIndex).mirror : definition.frames.get(curFrameIndex).mirror;
			final Color color = definition.frames.get(curFrameIndex).color;
			Color oldColor = null;
			if (color != null) {
				Game.batch().setColor(color);
				oldColor = Game.batch().getColor();
			}
			//			Game.batch().draw(frames.get(0), entity.position.x - origin.x, entity.position.y - origin.y);
			Game.batch().draw(frames.get(curFrameIndex), entity.position.x + (mirror ? definition.width : 0) - origin.x, entity.position.y + entity.position.z - origin.y, mirror ? -definition.width : definition.width, definition.height);
			if (color != null) {
				Game.batch().setColor(oldColor);
				Game.batch().setColor(Color.WHITE);
			}
		}
	}

	/** Resets an animation, invokes the frame change listener. */
	public void reset() {
		curFrameCount = curFrameIndex = 0;
		played = false;
		if (frameChangedListener != null) {
			frameChangedListener.invoke(definition.frames.get(curFrameIndex).particle);
		}
	}

	/** Sets a listener to invoke once the animation has run thru. Note, for
	 * animations with keeplast this should still be invoked.
	 * @param completeListener the listener */
	public void setCompleteListener(Listener completeListener) {
		this.completeListener = completeListener;
	}

	/** Sets a listener to invoke when an animation's frame changes.
	 * @param frameChangedListener the listener to invoke on frame change */
	public void setFrameChangedListener(TypeListener<String> frameChangedListener) {
		this.frameChangedListener = frameChangedListener;
	}

	/** Set to true to flip all x images in the definition
	 * @param mirrorAll whether or not to flip all */
	public void setMirrorAll(boolean mirrorAll) {
		this.mirrorAll = mirrorAll;
	}

	/** Sets the rendering origin of the sprite
	 * @param origin the new origin {@link Vector2} to use. Member fields are
	 *        set rather than tracking a pointer. */
	public void setOrigin(Vector2 origin) {
		this.origin.x = origin.x;
		this.origin.y = origin.y;
	}

	@Override
	public void update(Entity entity) {
		curFrameCount++;
		if (curFrameCount > definition.frames.get(curFrameIndex).duration) {
			curFrameCount = 0;
			curFrameIndex = (curFrameIndex + 1) % definition.frames.size;
			if (curFrameIndex == 0 && !played && completeListener != null) {
				completeListener.invoke();
				played = true;
			}
			if (definition.keepLast && curFrameIndex == 0) {
				curFrameIndex = definition.frames.size - 1;
				return;
			}
			if (frameChangedListener != null) {
				frameChangedListener.invoke(definition.frames.get(curFrameIndex).particle);
			}
		}
	}
}
