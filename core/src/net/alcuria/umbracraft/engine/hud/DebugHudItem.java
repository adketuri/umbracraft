package net.alcuria.umbracraft.engine.hud;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.BaseEntity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** A {@link BaseEntity} to display any debug information
 * @author Andrew Keturi */
public class DebugHudItem implements BaseEntity {

	private final BitmapFont font;
	private String fps;

	public DebugHudItem() {
		font = Game.assets().get("fonts/message.fnt", BitmapFont.class);
	}

	@Override
	public void render() {
		if (fps != null) {
			font.draw(Game.batch(), fps, 20, 20);
		}
	}

	@Override
	public void update() {
		fps = String.valueOf(Gdx.graphics.getFramesPerSecond());
	}

}
