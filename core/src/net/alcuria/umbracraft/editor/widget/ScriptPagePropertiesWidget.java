package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition.ScriptTrigger;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.editor.modules.ScriptListModule;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;

/** Displays and updates {@link ScriptPageDefinition} objects' preconditions. See
 * {@link ScriptListModule} for sample usage.
 * @author Andrew Keturi */
public class ScriptPagePropertiesWidget extends Module<ScriptPageDefinition> {

	private final Table content = new Table();
	private ScriptPageDefinition page;

	public Actor getActor() {
		return content;
	}

	@Override
	public String getTitle() {
		return "Preconditions";
	}

	@Override
	public void populate(Table content) {

	}

	private EventListener selected() {
		return new EventListener() {

			@Override
			public boolean handle(Event event) {
				if (event instanceof ChangeEvent) {
					page.trigger = ((SelectBox<ScriptTrigger>) event.getTarget()).getSelected();
					Game.log("Page trigger changed: " + page.trigger);
					return true;
				}
				return false;
			}
		};
	}

	public void setPage(ScriptPageDefinition page) {
		this.page = page;
		content.clear();
		content.setBackground(Drawables.get("blue"));
		content.add(new VisLabel("State Properties")).row();
		WidgetUtils.divider(content, "yellow");
		PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		config.suggestions = new ObjectMap<String, Array<String>>() {
			{
				put("animation", Editor.db().anims().keys().toArray());
				put("animationGroup", Editor.db().animGroups().keys());
				put("precondition", Editor.db().flags().keys());
			}
		};
		populate(content, ScriptPageDefinition.class, page, config);
		content.row();
		// create select box TODO: if we need another dropdown, make the generic populate handle this
		final VisSelectBox<ScriptTrigger> selectBox = new VisSelectBox<ScriptTrigger>();
		selectBox.setItems(ScriptTrigger.values());
		selectBox.setSelected(page.trigger);
		selectBox.addListener(selected());
		content.add(new Table() {
			{
				add(new VisLabel("trigger")).expandX().width(50);
				add(selectBox);
			}
		});
	}

}
