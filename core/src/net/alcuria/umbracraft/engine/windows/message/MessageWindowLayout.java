package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.windows.WindowLayout;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MessageWindowLayout extends WindowLayout {
	private VisLabel messageLabel;

	@Override
	public void create() {
		content.add(new Table() {
			{
				setBackground(Drawables.get("black"));
				add(messageLabel = new VisLabel("", new LabelStyle(Game.assets().get("fonts/message.fnt", BitmapFont.class), Color.WHITE))).expand().bottom();
			}
		}).expand().bottom().height(30);
	}

	public void setMessage(String message) {
		if (messageLabel != null) {
			messageLabel.setText(message);
		}
	}

}
