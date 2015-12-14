package net.alcuria.umbracraft.editor.widget;

import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScriptCommandsWidget {

	public static final Set<ScriptCommand> selected = new HashSet<ScriptCommand>();
	private final Table content = new Table(), commandList = new Table(), commandEntry = new Table();
	private ScriptPageDefinition currentPage;
	private ScriptCommandWidget widget;

	public Actor getActor() {
		return content;
	}

	public void setPage(final ScriptPageDefinition page) {
		currentPage = page;
		commandList.clear();
		commandEntry.clear();
		selected.clear();
		content.stack(commandList, commandEntry).expandX().fill();
		widget = new ScriptCommandWidget(commandList, commandEntry, page, page.command);
		widget.addActor();
	}

}
