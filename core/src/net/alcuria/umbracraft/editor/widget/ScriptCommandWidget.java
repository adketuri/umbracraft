package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.scripts.Commands;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;

public class ScriptCommandWidget {

	private final ScriptCommand command;
	private final Table content;
	private ScriptCommandWidget nextWidget;
	private final ScriptPageDefinition page;
	private final Listener rebuild;

	public ScriptCommandWidget(Table content, ScriptPageDefinition page, ScriptCommand command, Listener rebuild) {
		this.command = command;
		this.page = page;
		this.content = content;
		this.rebuild = rebuild;
	}

	public void addActor() {
		content.add(new Table() {
			{
				setBackground(Drawables.get("black"));
				add(new VisLabel("<> " + (command != null ? command.getName() : ""))).expandX().left().row();
				setTouchable(Touchable.enabled);
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if (getTapCount() > 1) {
							Game.log("double click");
							// insertion
							ScriptCommand newCommand = Commands.message("Hi");
							newCommand.setNext(command);
							Game.log(page.command + "");
							final ScriptCommand parent = page.getParent(page.command, command);
							if (parent != null) {
								parent.setNext(newCommand);
							} else {
								// no parent, this new command now becomes the HEAD
								page.command = newCommand;
							}
							page.printCommands();
							rebuild.invoke();
						}
					};
				});
				WidgetUtils.divider(this, "blue");
			}
		}).row();
		if (command != null) {
			nextWidget = new ScriptCommandWidget(content, page, command.getNext(), rebuild);
			nextWidget.addActor();
		}
	}
}
