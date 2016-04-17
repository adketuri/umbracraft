package net.alcuria.umbracraft.editor.layout;

import net.alcuria.umbracraft.definitions.enemy.EnemyGroupDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.modules.ListModule;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EnemyGroupListModule extends ListModule<EnemyGroupDefinition> {

	@Override
	public void addListItem() {
		final EnemyGroupDefinition enemyGroup = new EnemyGroupDefinition();
		enemyGroup.name = "Enemy Group " + rootDefinition.size();
		enemyGroup.enemies = new Array<String>();
		rootDefinition.add(enemyGroup);
	}

	@Override
	public void create(EnemyGroupDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 3;
		config.textFieldWidth = 200;
		populate(content, EnemyGroupDefinition.class, definition, config);
		content.row();
		content.add(new Table() {
			{
				add(WidgetUtils.tooltip("All enemies."));
				add(new VisLabel("Enemies:"));
			}
		}).row();
		WidgetUtils.divider(content, "blue");
		WidgetUtils.modifiableList(content, definition.enemies, new Array<String>(Editor.db().enemies().keys()));
	}

	@Override
	public String getTitle() {
		return "EnemyGroups";
	}

}
