package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.enemy.EnemyDefinition;
import net.alcuria.umbracraft.editor.Editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EnemyListModule extends ListModule<EnemyDefinition> {
	@Override
	public void addListItem() {
		final EnemyDefinition hero = new EnemyDefinition();
		hero.name = "Enemy " + rootDefinition.size();
		rootDefinition.add(hero);
	}

	@Override
	public void create(EnemyDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 3;
		config.textFieldWidth = 200;
		config.suggestions = new ObjectMap<String, Array<String>>();
		config.suggestions.put("animGroup", Editor.db().battleAnimGroups().keys());
		populate(content, EnemyDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "Enemies";
	}
}
