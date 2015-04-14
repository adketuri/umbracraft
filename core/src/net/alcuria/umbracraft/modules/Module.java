package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.widget.VisTextButton;

public abstract class Module {
	
	public VisTextButton button;
	public Definition definition;
	
	public Module() {
		button = new VisTextButton(getTitle());
	}
	
	public abstract String getTitle();
	
	public Button getButton() {
		return button;
	}
	
	public abstract void populate(Table content);

	public void save() {
		Json json = new Json();
		System.out.println(json.prettyPrint(definition));
	}

}
