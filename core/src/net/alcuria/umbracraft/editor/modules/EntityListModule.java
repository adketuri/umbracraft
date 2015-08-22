package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.ComponentType;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** A module to list out all our entities.
 * @author Andrew Keturi */
public class EntityListModule extends ListModule<EntityDefinition> {
	private EntityDefinition definition;
	private Table listTable, componentTable;

	@Override
	public void addListItem() {
		final EntityDefinition item = new EntityDefinition();
		item.components = new Array<ComponentDefinition>();
		rootDefinition.add(item);
	}

	/** @return the {@link ComponentDefinition} list */
	private Table components() {
		return new Table() {
			{
				for (final ComponentDefinition component : definition.components) {
					// populate component information
					defaults().expandX().fill();
					add(new VisLabel(component.getClass().getSimpleName())).row();
					add(new Table() {
						{
							defaults().expandX().fillX().pad(5);
							populate(this, component.getClass(), component, config());
							add(new VisTextButton("X") {
								{
									addListener(new ClickListener() {
										@Override
										public void clicked(InputEvent event, float x, float y) {
											definition.components.removeValue(component, true);
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
		};
	}

	private PopulateConfig config() {
		return new PopulateConfig() {
			{
				textFieldWidth = 200;
				suggestions = new ObjectMap<String, Array<String>>();
				suggestions.put("animationComponent", new Array<String>() {
					{
						final FileHandle handle = Gdx.files.external("umbracraft/animations.json");
						if (handle.exists()) {
							Array<AnimationDefinition> anims = new Json().fromJson(AnimationListDefinition.class, handle).animations;
							for (AnimationDefinition anim : anims) {
								add(anim.name);
							}
						}

					}
				});
			}
		};
	}

	@Override
	public void create(EntityDefinition definition, Table content) {
		this.definition = definition;
		populate(content, EntityDefinition.class, definition, new PopulateConfig());
		content.row();
		WidgetUtils.divider(content, "yellow");
		content.add(componentTable = new Table()).row();
		content.add(listTable = new Table());
		update();
	}

	@Override
	public String getTitle() {
		return "Entities";
	}

	/** @return a list containing all types of {@link ComponentDefinition} classes
	 *         as well as a button to add new ones. */
	private Table list() {
		final VisSelectBox<ComponentType> list = new VisSelectBox<ComponentType>();
		list.setItems(ComponentType.values());
		final VisTextButton button = new VisTextButton("Add Component");
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				try {
					definition.components.add(list.getSelected().clazz.newInstance());
					update();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
		return new Table() {
			{
				add(list);
				add(button).padLeft(20);
			}
		};
	}

	/** Updates the ui and repopulates all content */
	private void update() {
		listTable.clear();
		listTable.add(list());
		componentTable.clear();
		componentTable.add(components());
	}
}
