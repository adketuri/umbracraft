package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ScriptCommandWidget {

	private final ScriptCommand command;
	private Table content;

	public ScriptCommandWidget(ScriptCommand command) {
		this.command = command;
	}

	public Actor getActor() {
		if (content == null) {
			content = new Table() {
				{
					addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							if (getTapCount() > 1) {
								Game.log("Double clicked");
							}
						};
					});
				}
			};
		}
		return content;
	}

}
