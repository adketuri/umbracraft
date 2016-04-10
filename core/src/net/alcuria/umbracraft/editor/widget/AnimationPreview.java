package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** A helper image for displaying an {@link AnimationDefinition}
 * @author Andrew Keturi */
public class AnimationPreview extends Image {

	private int count = 0;
	private final AnimationDefinition definition;
	private int idx = 0;
	private String lastPath;
	private int originX = 0;
	private TextureRegion textureRegion;

	public AnimationPreview(AnimationDefinition definition) {
		this.definition = definition;
		originX = definition.originX;
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
			setColor(definition.frames.get(idx).color != null ? definition.frames.get(idx).color : Color.WHITE);
			final AnimationFrameDefinition frame = definition.frames.get(idx);
			final String path = "sprites/animations/" + definition.filename + ".png";
			if (Gdx.files.internal(path).exists() && definition.filename.length() > 0 && path != null && !path.equals(lastPath)) {
				textureRegion = new TextureRegion(new Texture(Gdx.files.internal(path)), frame.x * definition.width + (frame.mirror ? definition.width : 0), frame.y * definition.height, frame.mirror ? -definition.width : definition.width, definition.height);
				setDrawable(new TextureRegionDrawable(textureRegion));
				lastPath = new String(path);
			}
			if (textureRegion != null) {
				textureRegion.setRegion(frame.x * definition.width + (frame.mirror ? definition.width : 0), frame.y * definition.height, frame.mirror ? -definition.width : definition.width, definition.height);
			}
		}
	}

	public TextureRegion getCurrentRegion() {
		return textureRegion;
	}

	@Override
	public float getOriginX() {
		return originX;
	}

}
