package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.config.ConfigDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** The module for the configuration options.
 * @author Andrew Keturi */
public class ConfigModule extends Module<ConfigDefinition> {

	public ConfigModule() {
		super();
		load(ConfigDefinition.class);
	}

	@Override
	public String getTitle() {
		return "Configuration";
	}

	@Override
	public void populate(Table content) {
		populate(content, ConfigDefinition.class, rootDefinition, new PopulateConfig());

	}

}
