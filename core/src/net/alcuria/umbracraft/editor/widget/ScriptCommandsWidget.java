package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScriptCommandsWidget {

	private final Table content = new Table();
	private ScriptPageDefinition page;
	private final ScriptCommandWidget root = new ScriptCommandWidget();

	public Actor getActor() {
		return content;
	}

	public void setPage(final ScriptPageDefinition page) {
		this.page = page;
		content.clear();
		content.add(new Table() {
			{

			}
		});
	}

}
