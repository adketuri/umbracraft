package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition.ScriptTrigger;
import net.alcuria.umbracraft.editor.widget.ScriptPagePropertiesWidget;
import net.alcuria.umbracraft.editor.widget.ScriptPageWidget;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.engine.scripts.EmptyCommand;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

/** A module for displaying and editing scripts.
 * @author Andrew Keturi */
public class ScriptListModule extends ListModule<ScriptDefinition> {

	private final ScriptPageWidget commandsWidget = new ScriptPageWidget();
	private ScriptPageDefinition currentPage;
	private final Table headerTable = new Table(), preconditionsTable = new Table(), commandsTable = new Table();
	private final ScriptPagePropertiesWidget propertiesWidget = new ScriptPagePropertiesWidget();
	private ScriptDefinition script;

	@Override
	public void addListItem() {
		final ScriptDefinition script = new ScriptDefinition();
		script.name = "Script " + rootDefinition.size();
		script.pages = new Array<ScriptPageDefinition>();
		final ScriptPageDefinition page = new ScriptPageDefinition();
		page.trigger = ScriptTrigger.ON_INTERACTION;
		page.name = "Untitled";
		page.command = new EmptyCommand();
		script.pages.add(page);
		rootDefinition.add(script);
	}

	private Listener addPageListener() {
		return new Listener() {

			@Override
			public void invoke() {
				final ScriptPageDefinition page = new ScriptPageDefinition();
				page.command = new EmptyCommand();
				page.trigger = ScriptTrigger.ON_INTERACTION;
				page.name = "Untitled " + (script.pages.size + 1);
				script.pages.add(page);
				updateHeader();
			}
		};
	}

	@Override
	public void create(final ScriptDefinition definition, Table content) {
		script = definition;
		currentPage = script.pages.first();
		content.add(new Table() {
			{
				populate(this, ScriptDefinition.class, definition, new PopulateConfig());
			}
		}).row();
		content.add(headerTable).expandX().row();
		content.add(new Table() {
			{
				add(preconditionsTable).width(300).expand().fillX().top();
				add(commandsTable).width(600).expand().top().left();

			}
		}).expand().fill().minHeight(400);
		updateHeader();
		updatePreconditions();
		updateCommands();
	}

	@Override
	public String getTitle() {
		return "Scripts";
	}

	private Listener removePageListener() {
		return new Listener() {

			@Override
			public void invoke() {
				if (script.pages.size > 1) {
					script.pages.removeValue(currentPage, true);
					currentPage = script.pages.first();
					updateHeader();
				}
				updateHeader();
			}
		};
	}

	private Listener setPageListener(final ScriptPageDefinition page) {
		return new Listener() {

			@Override
			public void invoke() {
				currentPage = page;
				updateCommands();
				updatePreconditions();
				updateHeader();
			}
		};
	}

	private void updateCommands() {
		commandsTable.clear();
		commandsTable.add(commandsWidget.getActor()).expand().fill();
		commandsWidget.setPage(currentPage);
	}

	private void updateHeader() {
		headerTable.clear();
		headerTable.defaults().pad(5);
		for (final ScriptPageDefinition page : script.pages) {
			final TextButton pageButton = WidgetUtils.button(page.name, setPageListener(page));
			pageButton.getLabel().setColor(page == currentPage ? Color.YELLOW : Color.WHITE);
			headerTable.add(pageButton);
		}
		headerTable.add(WidgetUtils.button("+", addPageListener()));
		headerTable.add(WidgetUtils.button("-", removePageListener()));
	}

	private void updatePreconditions() {
		preconditionsTable.clear();
		preconditionsTable.add(propertiesWidget.getActor()).expandX().fill();
		propertiesWidget.setPage(currentPage);
	}
}
