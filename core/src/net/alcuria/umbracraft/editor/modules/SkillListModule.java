package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition.SkillActionType;
import net.alcuria.umbracraft.editor.widget.SkillTargetingWidget;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class SkillListModule extends ListModule<SkillDefinition> {

	private Table actionDropdownTable;
	private Table actionTable;
	private SkillDefinition definition;

	@Override
	public void addListItem() {
		final SkillDefinition skill = new SkillDefinition();
		skill.actions = new Array<SkillActionDefinition>();
		skill.name = "Skill " + rootDefinition.size();
		rootDefinition.add(skill);
	}

	private PopulateConfig config() {
		return new PopulateConfig() {
			{
				textFieldWidth = 200;
				cols = 1;
				suggestions = new ObjectMap<String, Array<String>>();

			}
		};
	}

	@Override
	public void create(final SkillDefinition definition, Table content) {
		this.definition = definition;
		final PopulateConfig config = new PopulateConfig();
		config.cols = 3;
		config.textFieldWidth = 200;
		populate(content, SkillDefinition.class, definition, config);
		content.row();
		content.add(new Table() {
			{
				add(new SkillTargetingWidget(definition).getActor());
				add(new Table() {
					{
						defaults().pad(20);
						add(actionTable = new Table()).row();
						add(actionDropdownTable = new Table()).row();
					}
				});
			}
		});
		update();

	}

	@Override
	public String getTitle() {
		return "Skills";
	}

	private void update() {
		actionTable.clear();
		actionTable.add(new Table() {
			{
				if (definition.actions != null) {
					for (final SkillActionDefinition action : definition.actions) {
						// populate component information
						defaults().expandX().fill();
						add(new VisLabel(StringUtils.formatName(action.getClass().getSimpleName()), Color.YELLOW)).row();
						add(new Table() {
							{
								defaults().expandX().fillX().pad(5);
								populate(this, action.getClass(), action, config());
								add(new VisTextButton("X") {
									{
										addListener(new ClickListener() {
											@Override
											public void clicked(InputEvent event, float x, float y) {
												definition.actions.removeValue(action, true);
												update();
											};
										});
									}
								}).width(20).expandX().right();
							}
						}).width(500);
						row();
						WidgetUtils.divider(this, "blue");
					}
				}
			}
		});
		actionDropdownTable.clear();
		actionDropdownTable.add(new Table() {
			{
				final VisSelectBox<SkillActionType> list = new VisSelectBox<SkillActionType>();
				list.setItems(SkillActionType.values());
				final VisTextButton button = new VisTextButton("Add Action");
				button.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						super.clicked(event, x, y);
						try {
							if (definition.actions == null) {
								definition.actions = new Array<SkillActionDefinition>();
							}
							definition.actions.add(list.getSelected().clazz.newInstance());
							update();
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				});
				add(list);
				add(button).padLeft(20);
			}
		});

	}
}
