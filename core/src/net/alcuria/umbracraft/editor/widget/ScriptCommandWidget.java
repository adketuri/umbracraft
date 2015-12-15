package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.engine.scripts.BattleScriptCommand;
import net.alcuria.umbracraft.engine.scripts.CameraTargetScriptCommand;
import net.alcuria.umbracraft.engine.scripts.LogScriptCommand;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand;
import net.alcuria.umbracraft.engine.scripts.MoveScriptCommand;
import net.alcuria.umbracraft.engine.scripts.PauseScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ShowAnimationScriptCommand;
import net.alcuria.umbracraft.engine.scripts.TeleportScriptCommand;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;

public class ScriptCommandWidget extends Module<ScriptCommand> {

	public static enum Commands {
		BATTLE("Battle", BattleScriptCommand.class), CAMERA_TARGET("Camera Target", CameraTargetScriptCommand.class), LOG("Log", LogScriptCommand.class), MESSAGE("Message", MessageScriptCommand.class), //
		MOVE("Move", MoveScriptCommand.class), PAUSE("Pause", PauseScriptCommand.class), SHOW_ANIM("Show Animation", ShowAnimationScriptCommand.class), TELEPORT("Teleport", TeleportScriptCommand.class);

		public static Commands from(final String name) {
			for (Commands c : Commands.values()) {
				if (c.name.equals(name)) {
					return c;
				}
			}
			return null;
		}

		public static Array<String> getAll() {
			return new Array<String>() {
				{
					for (Commands c : Commands.values()) {
						add(c.name);
					}
				}
			};
		}

		private Class<?> clazz;
		private String name;

		private Commands(String name, Class<?> clazz) {
			this.name = name;
			this.clazz = clazz;
		}

		public Class<?> getCommandClass() {
			return clazz;
		}

		public ScriptCommand getCommandInstance() {
			try {
				return (ScriptCommand) clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}

	private ScriptCommand command, newCommand;
	private final ScriptCommandsWidget commandsWidget;
	private final Table content, popup, popupFields = new Table();
	private ScriptCommandWidget nextWidget;
	private final ScriptPageDefinition page;

	public ScriptCommandWidget(ScriptCommandsWidget commandsWidget, Table content, Table popup, ScriptPageDefinition page, ScriptCommand command) {
		this.command = command;
		this.popup = popup;
		this.page = page;
		this.content = content;
		this.commandsWidget = commandsWidget;
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
							popup.setVisible(true);
							popup.clear();
							popupFields.clear();
							WidgetUtils.popupTitle(popup, "Add Command", closePopup());
							popup.setBackground(Drawables.get("black"));
							SuggestionWidget suggestionsWidget = new SuggestionWidget(Commands.getAll(), 100);
							popup.add(suggestionsWidget.getActor()).row();
							suggestionsWidget.addSelectListener(commandSelected(suggestionsWidget.getTextField()));
							suggestionsWidget.setGenericPopulate(commandSelected(suggestionsWidget.getTextField()));
							if (command != null) {
								//suggestionsWidget.getTextField().setText("Commands...");
							}
							popup.add(popupFields).row();
						}
					}

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
			nextWidget = new ScriptCommandWidget(commandsWidget, content, popup, page, command.getNext());
			nextWidget.addActor();
		}
	}

	private Listener closePopup() {
		return new Listener() {

			@Override
			public void invoke() {
				popup.setVisible(false);
			}
		};
	};

	private Listener commandCreated() {
		return new Listener() {

			@Override
			public void invoke() {
				// insertion
				newCommand.setNext(command);
				Game.log(page.command + "");
				final ScriptCommand parent = page.getParent(page.command, command);
				if (parent != null) {
					parent.setNext(newCommand);
				} else {
					// no parent, this new command now becomes the HEAD
					page.command = newCommand;
				}
				popup.setVisible(false);
				commandsWidget.setPage();
			}
		};
	}

	private Listener commandSelected(final VisTextField textField) {
		return new Listener() {

			@Override
			public void invoke() {
				// see if we have a real command
				Game.log(textField.getText() + "");
				Commands localCommand = Commands.from(textField.getText());
				if (localCommand != null) {
					popupFields.clear();
					if (newCommand == null) {
						Game.log("Setting new command");
						newCommand = localCommand.getCommandInstance();
					}
					populate(popupFields, localCommand.getCommandClass(), newCommand, new PopulateConfig());
					popupFields.add(WidgetUtils.button("Create", commandCreated()));
				} else {
					popupFields.clear();
					command = null;
				}
			}

		};
	}

	@Override
	public String getTitle() {
		return "Title";
	}

	@Override
	public void populate(Table content) {

	}
}
