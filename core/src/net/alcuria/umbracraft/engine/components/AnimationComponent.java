package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/** A component to display a single animation
 * @author Andrew Keturi */
public class AnimationComponent implements BaseComponent {

	private int ct, idx;
	private final AnimationDefinition definition;
	private Array<TextureRegion> frames;

	public AnimationComponent(AnimationDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create() {
		if (definition != null && frames == null) {
			Texture texture = Game.assets().get("sprites/animations/" + definition.filename, Texture.class);
			frames = new Array<TextureRegion>();
			for (AnimationFrameDefinition frame : definition.frames) {
				frames.add(new TextureRegion(new TextureRegion(texture, frame.x * definition.width, frame.y * definition.height, definition.width, definition.height)));
			}
		}
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(Entity object) {
		if (frames != null) {
			final boolean mirror = definition.frames.get(idx).mirror;
			Game.batch().draw(frames.get(idx), object.position.x + (mirror ? definition.width : 0), object.position.y + object.position.z, mirror ? -definition.width : definition.width, definition.height);
		}
	}

	@Override
	public void update(Entity object) {
		ct++;
		if (ct > definition.frames.get(idx).duration) {
			ct = 0;
			idx = (idx + 1) % definition.frames.size;
		}
	}
}
