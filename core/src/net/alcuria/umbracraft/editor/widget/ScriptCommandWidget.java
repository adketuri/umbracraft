package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;

public class ScriptCommandWidget extends Module<ScriptCommand> {

	private final ScriptCommand command;
	private final Table content;
	private ScriptCommandWidget nextWidget;
	private final ScriptPageDefinition page;
	private final Listener showPopup;

	public ScriptCommandWidget(Table content, ScriptPageDefinition page, ScriptCommand command, Listener showPopup) {
		this.command = command;
		this.page = page;
		this.content = content;
		this.showPopup = showPopup;
	}

	public void addActor() {

		final VisLabel label = new VisLabel("<> " + (command != null ? command.getName() : ""));

		content.add(new Table() {
			{
				add(label).expandX().left().row();
				setTouchable(Touchable.enabled);
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						ScriptCommandsWidget.selected.clear();
						ScriptCommandsWidget.selected.add(command);
						if (getTapCount() > 1) {
							Game.log("double click");
							// insertion
							//							ScriptCommand newCommand = new MessageScriptCommand("Hi");
							//							newCommand.setNext(command);
							//							Game.log(page.command + "");
							//							final ScriptCommand parent = page.getParent(page.command, command);
							//							if (parent != null) {
							//								parent.setNext(newCommand);
							//							} else {
							//								// no parent, this new command now becomes the HEAD
							//								page.command = newCommand;
							//							}
							showPopup.invoke();
						}
					};
				});
				WidgetUtils.divider(this, "blue");
			}

			@Override
			public void act(float delta) {
				super.act(delta);
				setBackground(ScriptCommandsWidget.selected.contains(command) ? Drawables.get("yellow") : Drawables.get("black"));
				label.setColor(ScriptCommandsWidget.selected.contains(command) ? Color.BLACK : Color.WHITE);
			}

		}).expandX().fill().row();
		if (command != null) {
			nextWidget = new ScriptCommandWidget(content, page, command.getNext(), showPopup);
			nextWidget.addActor();
		}
	}

	@Override
	public String getTitle() {
		return "Commands";
	}

	@Override
	public void populate(Table content) {

	}
}
