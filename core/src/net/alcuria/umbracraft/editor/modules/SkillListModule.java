package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition.SkillActionType;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.SkillTargetingWidget;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.TypeListener;
import net.alcuria.umbracraft.util.FileUtils;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class SkillListModule extends ListModule<SkillDefinition> {

	private Table actionDropdownTable, actionTable, iconTable;
	private SkillDefinition definition;

	@Override
	public void addListItem() {
		final SkillDefinition skill = new SkillDefinition();
		skill.actions = new Array<SkillActionDefinition>();
		skill.name = "Skill " + rootDefinition.size();
		rootDefinition.add(skill);
	}

	@Override
	public void create(final SkillDefinition definition, Table content) {
		this.definition = definition;
		populate(content, SkillDefinition.class, definition, skillConfig());
		content.defaults().pad(20);
		content.add(new Table() {
			{
				add(new Table() {
					{
						add(new VisLabel("Skill Icon:")).row();
						add(iconTable = new Table()).row();
						updateSkillIcon();
						WidgetUtils.divider(this, "yellow");
						add(new SkillTargetingWidget(definition).getActor()).row();
					}
				});
				add(new Table() {
					{
						defaults().pad(20);
						add(actionTable = new Table()).row();
						add(actionDropdownTable = new Table()).row();
					}
				});
			}
		});
		updateActions();

	}

	@Override
	public String getTitle() {
		return "Skills";
	}

	private PopulateConfig skillActionConfig() {
		return new PopulateConfig() {
			{
				labelWidth = 80;
				cols = 2;
				textFieldWidth = 80;
				suggestions = new ObjectMap<String, Array<String>>();
				suggestions.put("sound", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().soundPath, false));
			}
		};
	}

	private PopulateConfig skillConfig() {
		return new PopulateConfig() {
			{
				textFieldWidth = 200;
				cols = 1;
				suggestions = new ObjectMap<String, Array<String>>();
				suggestions.put("iconId", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().iconPath, false));
				listener = new TypeListener<String>() {

					@Override
					public void invoke(String type) {
						if (type.equals("iconId")) {
							updateSkillIcon();
						}
					}
				};
			}
		};
	}

	private void updateActions() {
		actionTable.clear();
		actionTable.add(new Table() {
			{
				if (definition.actions != null) {
					for (int i = 0; i < definition.actions.size; i++) {
						final SkillActionDefinition action = definition.actions.get(i);
						final int idx = i;
						defaults().expandX().fill();
						add(new VisLabel(StringUtils.formatName(action.getClass().getSimpleName()), Color.YELLOW)).row();
						add(new Table() {
							{
								defaults().expandX().fillX().pad(5);
								add(new Table() {
									{
										if (idx > 0) {
											add(new VisTextButton("^") {
												{
													addListener(new ClickListener() {
														@Override
														public void clicked(InputEvent event, float x, float y) {
															definition.actions.insert(idx - 1, definition.actions.removeIndex(idx));
															updateActions();
														};
													});
												}
											}).row();
										}
										if (idx < definition.actions.size - 1) {
											add(new VisTextButton("v") {
												{
													addListener(new ClickListener() {
														@Override
														public void clicked(InputEvent event, float x, float y) {
															definition.actions.insert(idx + 1, definition.actions.removeIndex(idx));
															updateActions();
														};
													});
												}
											}).row();
										}
									}
								}).expand(false, false).left();
								// populate component information
								populate(this, action.getClass(), action, skillActionConfig());
								add(new VisTextButton("X") {
									{
										addListener(new ClickListener() {
											@Override
											public void clicked(InputEvent event, float x, float y) {
												definition.actions.removeValue(action, true);
												updateActions();
											};
										});
									}
								}).width(20).expandX().right();
							}
						}).width(500).row();
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
							updateActions();
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

	private void updateSkillIcon() {
		iconTable.clear();
		String path = Editor.db().config().projectPath + Editor.db().config().iconPath + definition.iconId + ".png";
		if (Gdx.files.absolute(path).exists()) {
			final Texture texture = new Texture(Gdx.files.absolute(path));
			iconTable.add(new Image(texture)).size(texture.getWidth() * 2, texture.getHeight() * 2);
		}
	}
}
