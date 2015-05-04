package net.alcuria.umbracraft.widget;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** An image of a {@link AnimationFrameDefinition} that conforms to an
 * {@link AnimationDefinition}.
 * @author Andrew Keturi */
public class AnimationPreviewFrame extends Image {

	public AnimationPreviewFrame(AnimationDefinition definition, AnimationFrameDefinition frame) {
		update(definition, frame);
	}

	/** Updates the frame's image. Ensures the file exists before attempting to
	 * set the drawable.
	 * @param definition
	 * @param frame */
	public void update(AnimationDefinition definition, AnimationFrameDefinition frame) {
		final String path = "sprites/animations/" + definition.filename;
		if (Gdx.files.internal(path).exists()) {
			Texture texture = new Texture(Gdx.files.internal(path));
			TextureRegion region = new TextureRegion(texture, frame.x * definition.frameWidth, frame.y * definition.frameHeight, definition.frameWidth, definition.frameHeight);
			setDrawable(new TextureRegionDrawable(region));
		}

	}

}
