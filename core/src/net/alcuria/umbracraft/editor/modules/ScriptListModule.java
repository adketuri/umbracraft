package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** A module for displaying and editing scripts.
 * @author Andrew Keturi */
public class ScriptListModule extends ListModule<ScriptDefinition> {

	@Override
	public void addListItem() {

	}

	@Override
	public void create(ScriptDefinition definition, Table content) {

	}

	@Override
	public String getTitle() {
		return "Scripts";
	}

}
