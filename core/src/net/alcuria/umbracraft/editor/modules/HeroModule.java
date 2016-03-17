package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.hero.HeroDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** The module for heroes.
 * @author Andrew Keturi */
public class HeroModule extends ListModule<HeroDefinition> {

	@Override
	public void addListItem() {
		final HeroDefinition hero = new HeroDefinition();
		hero.name = "Hero " + rootDefinition.size();
		rootDefinition.add(hero);
	}

	@Override
	public void create(HeroDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 3;
		config.textFieldWidth = 200;
		populate(content, HeroDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "Heroes";
	}

}
