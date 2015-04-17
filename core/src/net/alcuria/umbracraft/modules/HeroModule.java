package net.alcuria.umbracraft.modules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import net.alcuria.umbracraft.definitions.HeroDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

public class HeroModule extends Module<HeroDefinition> {

	public HeroModule() {
		super();
		if (definition == null) {
			definition = new HeroDefinition();
		}
	}

	@Override
	public String getTitle() {
		return "Heroes";
	}

	@Override
	public void load() {
		Json json = new Json();
		final FileHandle handle = Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json");
		if (handle.exists()) {
			definition = json.fromJson(HeroDefinition.class, handle);
		}
	}

	@Override
	public void populate(Table content) {
		try {
			content.add(new Table() {
				{
					ArrayList<Field> fieldList = new ArrayList<Field>();
					Class<HeroDefinition> tmpClass = HeroDefinition.class;
					fieldList.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
					for (final Field field : fieldList) {
						add(new Table() {
							{
								add(new VisLabel(field.getName()));
								final String value = definition != null && field.get(definition) != null ? String.valueOf(field.get(definition)) : "";
								final VisTextArea textArea = new VisTextArea(value);
								textArea.setTextFieldListener(new TextFieldListener() {

									@Override
									public void keyTyped(
											VisTextField textField, char c) {
										try {
											if (field.getType().toString().equals("int")) {
												field.setInt(definition, Integer.valueOf(textField.getText()));
											} else {
												field.set(definition,textField.getText());
											}
										} catch (IllegalArgumentException e) {
											e.printStackTrace();
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										}

									}
								});
								add(textArea);
							}
						}).row();
					}
				}
			}).expand().fill();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
