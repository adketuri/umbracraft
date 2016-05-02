package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.editor.Editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
		if (definition != null && definition.filename != null && definition.filename.length() > 0) {
			final String path = "sprites/animations/" + definition.filename + ".png";
			FileHandle handle = null;
			if (Gdx.files.internal(path).exists()) {
				handle = Gdx.files.internal(path);
			} else {
				final String extPath = Editor.db().config().projectPath + Editor.db().config().spritePath + definition.filename + ".png";
				if (Gdx.files.absolute(extPath).exists()) {
					handle = Gdx.files.absolute(extPath);
				}
			}
			if (handle != null) {
				Texture texture = new Texture(handle);
				TextureRegion region = new TextureRegion(texture, frame.x * definition.width + (frame.mirror ? definition.width : 0), frame.y * definition.height, frame.mirror ? -definition.width : definition.width, definition.height);
				setDrawable(new TextureRegionDrawable(region));
			}

		}

	}

}
