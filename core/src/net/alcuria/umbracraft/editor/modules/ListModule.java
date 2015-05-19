package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.ListDefinition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** Make this abstract, then have some create method called to populate the
 * content
 * @author Andrew Keturi
 * @param <T> */
public abstract class ListModule<T extends Definition> extends Module<ListDefinition> {
	private T definition;
	private Table menu;
	private final Table view;

	public ListModule() {
		super();
		load(ListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new ListDefinition<T>();
		}
		view = new Table();
	}

	public abstract void addListItem();

	public abstract void create(T definition, Table content);

	@Override
	public abstract String getTitle();

	private Table menu() {
		return new Table() {
			{

				defaults().uniformX().fillX().expandX();
				for (int i = 0; i < rootDefinition.size(); i++) {
					final int idx = i;
					final VisTextButton button = new VisTextButton(rootDefinition.get(i).getName());
					button.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							view.clear();
							create((T) rootDefinition.get(idx), view);
						};
					});
					add(button).row();
				}
				add().expand().fill().row();
				final VisTextButton button = new VisTextButton("Add Item");
				button.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						addListItem();
						menu.clear();
						menu.add(menu()).expandY().fill().padLeft(5).top();
					}

				});
				add(button).padBottom(20).row();

			}
		};
	}

	@Override
	public void populate(Table content) {
		content.add(menu = menu()).expandY().fill().padLeft(5).top();
		view.clear();
		create(null, view);
		content.add(view).expand().top();
	}
}
