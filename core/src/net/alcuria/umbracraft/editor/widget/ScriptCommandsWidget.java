package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScriptCommandsWidget {

	private final Table content = new Table();
	private ScriptPageDefinition currentPage;
	private ScriptCommandWidget widget;

	public Actor getActor() {
		return content;
	}

	public void setPage(final ScriptPageDefinition page) {
		currentPage = page;
		content.clear();
		content.defaults().expandX().fill();
		widget = new ScriptCommandWidget(content, page, page.command, updateListener());
		widget.addActor();

	}

	private Listener updateListener() {
		return new Listener() {

			@Override
			public void invoke() {
				setPage(currentPage);
			}
		};
	}

}
