package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.hero.HeroDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** The module for heroes.
 * @author Andrew Keturi */
public class HeroModule extends Module<HeroDefinition> {

	public HeroModule() {
		super();
		load(HeroDefinition.class);
	}

	@Override
	public String getTitle() {
		return "Heroes";
	}

	@Override
	public void populate(Table content) {
		populate(content, HeroDefinition.class, rootDefinition, new PopulateConfig());
	}

}
