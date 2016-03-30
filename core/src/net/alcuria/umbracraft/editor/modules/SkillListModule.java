package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.skill.SkillDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SkillListModule extends ListModule<SkillDefinition> {

	@Override
	public void addListItem() {
		final SkillDefinition skill = new SkillDefinition();
		skill.name = "Skill " + rootDefinition.size();
		rootDefinition.add(skill);
	}

	@Override
	public void create(SkillDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 3;
		config.textFieldWidth = 200;
		populate(content, SkillDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "Skills";
	}

}
