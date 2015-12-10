package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.ScriptListModule;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;

/** Displays and updates {@link ScriptPageDefinition} objects' preconditions. See
 * {@link ScriptListModule} for sample usage.
 * @author Andrew Keturi */
public class ScriptPreconditionsWidget {

	private final Table content = new Table();
	private ScriptPageDefinition page;

	public Actor getActor() {
		return content;
	}

	public void setPage(ScriptPageDefinition page) {
		this.page = page;
		content.clear();
		content.setBackground(Drawables.get("blue"));
		content.add(new VisLabel("Preconditions")).expand().fill().row();
		WidgetUtils.divider(content, "yellow");
		content.add(new VisLabel("Page: " + page.name));
	}

}
