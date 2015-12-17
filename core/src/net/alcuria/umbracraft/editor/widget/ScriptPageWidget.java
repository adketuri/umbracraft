package net.alcuria.umbracraft.editor.widget;

import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScriptPageWidget {

	public static final Set<ScriptCommand> selected = new HashSet<ScriptCommand>();
	private Table content, commandList, commandEntry;
	private ScriptPageDefinition currentPage;
	private ScriptCommandWidget widget;

	public Actor getActor() {
		if (content == null) {
			content = new Table() {
				@Override
				public void act(float delta) {
					super.act(delta);
					if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && commandEntry.isVisible()) {
						commandEntry.setVisible(false);
					}
				}
			};
			content.stack(commandList = new Table(), commandEntry = new Table()).minHeight(500).expand().top().fillX().row();
		}
		return content;
	}

	public void setPage() {
		setPage(currentPage);
	}

	public void setPage(final ScriptPageDefinition page) {
		currentPage = page;
		commandList.clear();
		commandEntry.clear();
		selected.clear();
		widget = new ScriptCommandWidget(this, commandList, commandEntry, page, page.command);
		widget.addActor();
		commandList.row();
		commandList.add().expand().fill();
		commandEntry.setVisible(false);
	}

}
