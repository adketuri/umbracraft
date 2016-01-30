package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

/** Displays debugging info
 * @author Andrew Keturi */
public class DebugText extends OnscreenInput {

	private final LabelStyle style = new LabelStyle(Game.assets().get("fonts/message.fnt", BitmapFont.class), Color.WHITE);
	private final Table table;
	private final Array<Label> text = new Array<Label>();

	public DebugText(Stage stage) {
		table = new Table();
		stage.addActor(table);
		for (int i = 0; i < 10; i++) {
			Label l = new Label("", style);
			table.add(l).expandX().left().row();
			text.add(l);
		}
	}

	@Override
	public void update() {
		super.update();
		table.setVisible(Game.isDebug());
		if (table.isVisible()) {
			table.setPosition(10, Config.viewHeight - 100);
			text.get(0).setText("CamX: " + Game.view().getCamera().position.x);
			text.get(1).setText("CamY: " + Game.view().getCamera().position.y);
			final Rectangle bounds = Game.view().getBounds();
			if (bounds != null) {
				text.get(2).setText("Bounds: " + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height);
			}
			text.get(3).setText("Map Size: " + Game.map().getWidth() + ", " + Game.map().getHeight());
		}
	}

}