package net.alcuria.umbracraft.editor.modules;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.editor.widget.SuggestionWidget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

/** The base class of a module. Modules contain UI to modify {@link Definition}
 * classes.
 * @author Andrew Keturi
 * @param <T> */
public abstract class Module<T extends Definition> {

	/** A helper class to define layout configurations when populating
	 * definition.
	 * @author Andrew Keturi */
	public static class PopulateConfig {
		/** Columns for each field */
		public int cols = 3;
		/** Width of the label */
		public int labelWidth = 130;
		/** A custom listener to invoke when a field changes value */
		public Listener listener;
		public ObjectMap<String, Array<String>> suggestions;
		/** Width of the text fields */
		public int textFieldWidth = 100;
	}

	public VisTextButton button;
	protected T rootDefinition;

	public Module() {
		button = new VisTextButton(getTitle());
	}

	public Button getButton() {
		return button;
	}

	public abstract String getTitle();

	public void load(final Class<T> clazz) {
		Json json = new Json();
		json.setIgnoreUnknownFields(true);
		final FileHandle handle = Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json");
		if (handle.exists()) {
			rootDefinition = json.fromJson(clazz, handle);
		} else {
			try {
				rootDefinition = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void populate(Table content);

	/** A generic populate method. Populates a table with a class's fields which
	 * may be modified. For ints and string fields, a textfield is created where
	 * the value of the field is updated when the textfield changes. For boolean
	 * fields a checkbox is used. Other objects and private fields are not
	 * displayed.
	 * @param content the table to update
	 * @param clazz the class definitions
	 * @param definition the definition we want to update (when fields change
	 *        and so on)
	 * @param config configuration settings for how the populate is handled */
	public void populate(final Table content, final Class<?> clazz, final Definition definition, final PopulateConfig config) {
		assert (config.cols > 0);
		try {
			content.add(new Table() {
				{
					int idx = 0;
					ArrayList<Field> fieldList = new ArrayList<Field>();
					fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
					for (int i = 0; i < fieldList.size(); i++) {
						final Field field = fieldList.get(i);
						if (field.getModifiers() != Modifier.PRIVATE && (field.getType().toString().equals("int") || field.getType().toString().equals("float") || field.getType() == String.class || field.getType().toString().equals("boolean"))) {
							if (idx % config.cols == config.cols - 1) {
								add(keyInput(definition, field)).row();
							} else {
								add(keyInput(definition, field));
							}
							idx++;
						}
					}
					int size = fieldList.size();
					while (size % config.cols != 0) {
						add();
						size++;
					}
				}

				private Table keyInput(final Definition definition, final Field field) {
					return new Table() {
						{
							if (field.getType().toString().equals("boolean")) {
								add(new VisLabel(field.getName())).minWidth(config.labelWidth);
								boolean value = false;
								try {
									value = Boolean.valueOf(field.getBoolean(definition));
								} catch (Exception e) {
								}
								final VisCheckBox checkBox = new VisCheckBox("", value);
								checkBox.addListener(new ClickListener() {
									@Override
									public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
										try {
											field.setBoolean(definition, checkBox.isChecked());
											if (config.listener != null) {
												config.listener.invoke();
											}
										} catch (IllegalArgumentException | IllegalAccessException e) {
											e.printStackTrace();
										}
									};
								});
								checkBox.align(Align.left);
								add(checkBox).width(config.textFieldWidth).expandX().fill().left();

							} else if (field.getType().toString().equals("int") || field.getType().toString().equals("float") || field.getType() == String.class) {
								add(new VisLabel(field.getName())).minWidth(config.labelWidth);
								String value;
								try {
									value = String.valueOf(field.get(definition));
								} catch (Exception e) {
									value = "";
								}
								if ("null".equals(value)) {
									value = "";
								}
								VisTextField textField = null;
								SuggestionWidget widget = null;
								if (config.suggestions != null && config.suggestions.containsKey(field.getName())) {
									widget = new SuggestionWidget(config.suggestions.get(field.getName()), config.textFieldWidth);
									add(widget.getActor()).width(config.textFieldWidth);
									textField = widget.getTextField();
									textField.setText(value);
									final VisTextField tf = textField;
									widget.addSelectListener(new Listener() {

										@Override
										public void invoke() {
											saveField(field, definition, tf, config);
										}

									});

								} else {
									textField = new VisTextField(value);
									add(textField).width(config.textFieldWidth);
								}
								final SuggestionWidget w = widget;
								textField.setTextFieldListener(new TextFieldListener() {

									@Override
									public void keyTyped(VisTextField textField, char c) {
										saveField(field, definition, textField, config);
										if (w != null) {
											w.populateSuggestions();
										}
									}
								});

							}

						}
					};
				}
			}).expandX().fill();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(rootDefinition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}

	public void saveField(Field field, Definition definition, VisTextField textField, PopulateConfig config) {
		try {
			if (field.getType().toString().equals("int")) {
				if (textField.getText().equals("")) {
					field.setInt(definition, 0);
				} else {
					field.setInt(definition, Integer.valueOf(textField.getText()));
				}
			} else if (field.getType().toString().equals("float")) {
				if (textField.getText().equals("")) {
					field.setFloat(definition, 0);
				} else {
					field.setFloat(definition, Float.valueOf(textField.getText()));
				}
			} else {
				field.set(definition, textField.getText());
			}
			if (config.listener != null) {
				config.listener.invoke();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("null pointer");
		}
	}
}
