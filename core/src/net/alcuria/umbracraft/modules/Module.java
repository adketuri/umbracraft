package net.alcuria.umbracraft.modules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

public abstract class Module<T extends Definition> {

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
		final FileHandle handle = Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json");
		if (handle.exists()) {
			rootDefinition = json.fromJson(clazz, handle);
		}
	}

	public abstract void populate(Table content);

	public void populate(Table content, final Class<?> clazz, final Definition definition) {
		try {
			content.add(new Table() {
				{
					ArrayList<Field> fieldList = new ArrayList<Field>();
					fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
					for (int i = 0; i < fieldList.size(); i++) {
						final Field field = fieldList.get(i);
						if (i % 3 == 2) {
							add(keyInput(definition, field)).row();
						} else {
							add(keyInput(definition, field));
						}
					}
					int size = fieldList.size();
					while (size % 3 != 0) {
						add();
						size++;
					}
				}

				private Table keyInput(final Definition definition, final Field field) {
					return new Table() {
						{
							add(new VisLabel(field.getName())).width(130);
							String value;
							try {
								value = String.valueOf(field.get(definition));
							} catch (Exception e) {
								value = "";
							}
							final VisTextArea textArea = new VisTextArea(value);
							textArea.setTextFieldListener(new TextFieldListener() {

								@Override
								public void keyTyped(VisTextField textField, char c) {
									try {
										if (field.getType().toString().equals("int")) {
											field.setInt(definition, Integer.valueOf(textField.getText()));
										} else {
											field.set(definition, textField.getText());
										}
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									}

								}
							});
							add(textArea).width(100);
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
}
