package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.Module;
import net.alcuria.umbracraft.engine.scripts.BattleScriptCommand;
import net.alcuria.umbracraft.engine.scripts.CameraTargetScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ControlVariableCommand;
import net.alcuria.umbracraft.engine.scripts.FlagScriptCommand;
import net.alcuria.umbracraft.engine.scripts.LogScriptCommand;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand;
import net.alcuria.umbracraft.engine.scripts.MoveScriptCommand;
import net.alcuria.umbracraft.engine.scripts.PauseScriptCommand;
import net.alcuria.umbracraft.engine.scripts.RemoveEntityCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ShowAnimationScriptCommand;
import net.alcuria.umbracraft.engine.scripts.TeleportScriptCommand;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
		BATTLE("Battle", BattleScriptCommand.class), CAMERA_TARGET("Camera Target", CameraTargetScriptCommand.class), FLAG("Flag", FlagScriptCommand.class), LOG("Log", LogScriptCommand.class), //
		MESSAGE("Message", MessageScriptCommand.class), MOVE("Move", MoveScriptCommand.class), PAUSE("Pause", PauseScriptCommand.class), REMOVE("Remove Entity", RemoveEntityCommand.class), //
		SHOW_ANIM("Show Animation", ShowAnimationScriptCommand.class), TELEPORT("Teleport", TeleportScriptCommand.class), VARIABLE("Control Variable", ControlVariableCommand.class);

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

		/** Given a {@link ScriptCommand}, gets the {@link String} representation
		 * @param command a {@link ScriptCommand}
		 * @return the {@link String} name */
		public static String getNameFrom(ScriptCommand command) {
			for (Commands c : Commands.values()) {
				if (c.clazz == command.getClass()) {
					return c.name;
				}
			}
			return null;
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
				e.printStackTrace();
			}
			return null;
		}

	}

	private static boolean consumedDown = false;
	private final ScriptCommand command;
	private final ScriptPageWidget commandsWidget;
	private final Table content, popup, buttonTable = new Table(), popupFields = new Table();
	private ScriptCommand createdCommand;
	private ScriptCommandWidget nextWidget;
	private final ScriptPageDefinition page;
	private SuggestionWidget suggestionsWidget;

	public ScriptCommandWidget(ScriptPageWidget commandsWidget, Table content, Table popup, ScriptPageDefinition page, ScriptCommand command) {
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
						ScriptPageWidget.selected.clear();
						ScriptPageWidget.selected.add(command);
						if (getTapCount() > 1) {
							createPopup("Insert Command", null);
						}
					}

				});
				WidgetUtils.divider(this, "blue");
			}

			@Override
			public void act(float delta) {
				super.act(delta);
				// without this, weird stuff happens with multiple down keys triggering. okay.
				if (consumedDown && !Gdx.input.isKeyPressed(Keys.DOWN)) {
					consumedDown = false;
				}

				// update bg based on this widget's selected state
				setBackground(ScriptPageWidget.selected.contains(command) ? Drawables.get("yellow") : Drawables.get("black"));
				label.setColor(ScriptPageWidget.selected.contains(command) ? Color.BLACK : Color.WHITE);

				// handle keys
				if (command != null && ScriptPageWidget.selected.contains(command) && !popup.isVisible()) {
					if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
						Game.log("Editing...");
						createPopup("Edit Command", Commands.getNameFrom(command));
						populate(popupFields, command.getClass(), command, populateConfig());
						buttonTable.clear();
						buttonTable.add(WidgetUtils.button("Update", commandUpdated()));
					} else if (Gdx.input.isKeyJustPressed(Keys.DEL) || Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
						Game.log("Deleting...");
						final ScriptCommand next = command.getNext();
						final ScriptCommand parent = page.getParent(page.command, command);
						if (parent != null) {
							parent.setNext(next);
						} else {
							// no parent, this new command now becomes the HEAD
							page.command = next;
						}
						commandsWidget.setPage();
					} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
						Game.log("Pressed up");
						final ScriptCommand parent = page.getParent(page.command, command);
						if (parent != null) {
							ScriptPageWidget.selected.clear();
							ScriptPageWidget.selected.add(parent);
						}
					} else if (Gdx.input.isKeyJustPressed(Keys.DOWN) && !consumedDown) {
						consumedDown = true;
						if (command.getNext() != null) {
							Game.log("Pressed down, " + command != null ? command.getName() : "");
							ScriptPageWidget.selected.clear();
							ScriptPageWidget.selected.add(command.getNext());
						}
					}
				}
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
				createdCommand.setNext(command);
				final ScriptCommand parent = page.getParent(page.command, command);
				Game.log("INSERTING: createdCommand: " + (createdCommand != null ? createdCommand.getName() : "null") + " command:" + (command != null ? command.getName() : "null") + " parent: " + (parent != null ? parent : "null"));
				if (parent != null) {
					parent.setNext(createdCommand);
				} else {
					// no parent, this new command now becomes the HEAD
					page.command = createdCommand;
				}
				popup.setVisible(false);
				commandsWidget.setPage();
			}
		};
	}

	private Listener commandUpdated() {
		return new Listener() {

			@Override
			public void invoke() {
				// update
				popup.setVisible(false);
				commandsWidget.setPage();
			}
		};
	}

	private void createPopup(final String title, final String defCommand) {
		popup.setVisible(true);
		popup.clear();
		popupFields.clear();
		popup.setBackground(Drawables.get("black"));
		WidgetUtils.popupTitle(popup, title, closePopup());
		suggestionsWidget = new SuggestionWidget(Commands.getAll(), 130);
		popup.add(new Table() {
			{
				add(new VisLabel("Enter Command:"));
				add(suggestionsWidget.getActor());
				if (defCommand != null) {
					suggestionsWidget.getTextField().setText(defCommand);
				}
				if (title.contains("Edit")) { // lol dw bout it
					suggestionsWidget.getTextField().setDisabled(true);
				}
			}
		}).row();
		suggestionsWidget.addSelectListener(createPopupFields(suggestionsWidget.getTextField()));
		suggestionsWidget.setGenericPopulate(createPopupFields(suggestionsWidget.getTextField()));
		popup.add(popupFields).expand().fill().row();
		popup.add(buttonTable);
	}

	private Listener createPopupFields(final VisTextField textField) {
		return new Listener() {
			private String last = "";

			@Override
			public void invoke() {
				// see if we should really repopulate
				if (!last.equals(textField.getText())) {
					Game.log("Command changed. Attempting rebuild.");
					// see if we have a real command
					Commands localCommand = Commands.from(textField.getText());
					if (localCommand != null) {
						popupFields.clear();
						if (createdCommand == null) {
							Game.log("Setting new command");
							createdCommand = localCommand.getCommandInstance();
						}
						populate(popupFields, localCommand.getCommandClass(), createdCommand, populateConfig());
						buttonTable.clear();
						buttonTable.add(WidgetUtils.button("Create", commandCreated()));
					} else {
						popupFields.clear();
						createdCommand = null;
					}
				}
				last = textField.getText();
			}

		};
	}

	@Override
	public String getTitle() {
		return "Title";
	}

	/** Updates the filters if we change the "type" dropdown
	 * @param textField
	 * @return */
	private TypeListener<String> listener(final VisTextField textField) {
		return new TypeListener<String>() {

			@Override
			public void invoke(String type) {
				if ("type".equals(type)) {
					Commands localCommand = Commands.from(textField.getText());
					popupFields.clear();
					populate(popupFields, localCommand.getCommandClass(), createdCommand, populateConfig());
				}
			}
		};
	}

	@Override
	public void populate(Table content) {

	}

	private PopulateConfig populateConfig() {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		config.textFieldWidth = 300;
		config.labelWidth = 100;
		// depending on whether or not we edit or create one or the other createdCommand/command is null ugghh idk why right now but this fixes stuff
		if (createdCommand == null) {
			createdCommand = command;
		}
		config.suggestions = createdCommand.getSuggestions();
		config.filter = createdCommand.getFilter();
		if (suggestionsWidget != null) {
			config.listener = listener(suggestionsWidget.getTextField());
		}
		return config;
	}
}
