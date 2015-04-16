package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.VisTextButton;

public abstract class Module<T extends Definition> {

	T definition;
	public VisTextButton button;

	public Module() {
		button = new VisTextButton(getTitle());
		load();
	}

	public abstract String getTitle();


	public Button getButton() {
		return button;
	}

	public abstract void populate(Table content);

	public void save() {
		Json json = new Json();
		String jsonStr = json.prettyPrint(definition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}

	public abstract void load();
}
