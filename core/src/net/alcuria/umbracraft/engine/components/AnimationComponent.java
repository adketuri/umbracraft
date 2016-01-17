package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** A component to display a single animation.
 * @author Andrew Keturi */
public class AnimationComponent implements Component {

	private float alpha = 1;
	private Listener completeListener;
	private int ct, idx;
	private final AnimationDefinition definition;
	private Array<TextureRegion> frames;
	private boolean mirrorAll;
	private final Vector2 origin = new Vector2();
	private boolean played = false;

	public AnimationComponent(AnimationDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create(Entity entity) {
		if (definition != null) {
			Texture texture = Game.assets().get("sprites/animations/" + definition.filename, Texture.class);
			frames = new Array<TextureRegion>();
			for (AnimationFrameDefinition frame : definition.frames) {
				frames.add(new TextureRegion(new TextureRegion(texture, frame.x * definition.width, frame.y * definition.height, definition.width, definition.height)));
			}
			origin.x = definition.originX;
			origin.y = definition.originY;
			ct = idx = 0;
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {
		if (frames != null) {
			final boolean mirror = mirrorAll ? !definition.frames.get(idx).mirror : definition.frames.get(idx).mirror;
			if (alpha != 1) {
				Game.batch().flush();
				Game.batch().setColor(1, 1, 1, alpha);
			}
			//			Game.batch().draw(frames.get(0), entity.position.x - origin.x, entity.position.y - origin.y);
			Game.batch().draw(frames.get(idx), entity.position.x + (mirror ? definition.width : 0) - origin.x, entity.position.y + entity.position.z - origin.y, mirror ? -definition.width : definition.width, definition.height);
			if (alpha != 1) {
				Game.batch().flush();
				Game.batch().setColor(Color.WHITE);
			}
		}
	}

	public void reset() {
		ct = idx = 0;
		played = false;
	}

	/** Sets the alpha of the animation when rendering
	 * @param alpha the alpha, from 0 -1 (inclusive) */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	/** Sets a listener to invoke once the animation has run thru. Note, for
	 * animations with keeplast this should still be invoked.
	 * @param completeListener the listener */
	public void setListener(Listener completeListener) {
		this.completeListener = completeListener;
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
		ct++;
		if (ct > definition.frames.get(idx).duration) {
			ct = 0;
			idx = (idx + 1) % definition.frames.size;
			if (idx == 0 && !played && completeListener != null) {
				completeListener.invoke();
				played = true;
			}
			if (definition.keepLast && idx == 0) {
				idx = definition.frames.size - 1;
			}
		}
	}
}
