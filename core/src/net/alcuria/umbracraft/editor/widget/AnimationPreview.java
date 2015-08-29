package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** A helper image for displaying an {@link AnimationDefinition}
 * @author Andrew Keturi */
public class AnimationPreview extends Image {

	private int count = 0;
	private TextureRegion currentRegion;
	private final AnimationDefinition definition;
	private int idx = 0;

	public AnimationPreview(AnimationDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (definition.frames.size == 0) {
			return;
		}
		count++;
		if (idx >= 0 && idx < definition.frames.size && count > definition.frames.get(idx).duration) {
			count = 0;
			idx = (idx + 1) % definition.frames.size;
			final AnimationFrameDefinition frame = definition.frames.get(idx);
			final String path = "sprites/animations/" + definition.filename;
			if (Gdx.files.internal(path).exists()) {
				Texture texture = new Texture(Gdx.files.internal(path));
				currentRegion = new TextureRegion(texture, frame.x * definition.width + (frame.mirror ? definition.width : 0), frame.y * definition.height, frame.mirror ? -definition.width : definition.width, definition.height);
				setDrawable(new TextureRegionDrawable(currentRegion));
			}
		}
	}

	public TextureRegion getCurrentRegion() {
		return currentRegion;
	}

}
