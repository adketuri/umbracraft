package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.kotcrab.vis.ui.widget.VisTextButton;

public abstract class Module<T extends Definition> {

	public VisTextButton button;
	protected T definition;

	public Module() {
		button = new VisTextButton(getTitle());
		load();
	}

	public Button getButton() {
		return button;
	}

	public abstract String getTitle();

	public abstract void load();

	public abstract void populate(Table content);

	public void save() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(definition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}
}
