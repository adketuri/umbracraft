package net.alcuria.umbracraft.editor.modules;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** A module to display a generic list of some definition types.
 * @author Andrew Keturi
 * @param <T> */
@SuppressWarnings("rawtypes")
public abstract class ListModule<T extends Definition> extends Module<ListDefinition> {
	private final Array<VisTextButton> buttons = new Array<VisTextButton>();
	private Definition currentIndex;
	private T definition;
	private final Set<String> expandedTags = new HashSet<String>();
	private Table menu;
	private final Array<Definition> sortedDefinitions = new Array<Definition>();
	private final Table view;

	public ListModule() {
		super();
		load(ListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new ListDefinition<T>();
		}
		view = new Table();
	}

	/** Adds an item to the list of definitions. */
	public abstract void addListItem();

	private void clearView() {
		view.clear();
		view.add(new VisLabel("Select an item from the left."));
	}

	/** Creates the ui in the right pane
	 * @param definition the definition to use
	 * @param content the {@link Table} containing the content */
	public abstract void create(T definition, Table content);

	@Override
	public abstract String getTitle();

	private Table menu() {
		return new Table() {

			{
				update();
			}

			private Listener addListener() {
				return new Listener() {

					@Override
					public void invoke() {
						addListItem();
						menu.clear();
						menu.add(menu()).expandY().fill().padLeft(5).top();
					}
				};
			}

			private Listener deleteListener() {
				return new Listener() {

					@Override
					public void invoke() {
						if (currentIndex != null) {
							rootDefinition.delete(currentIndex);
							currentIndex = null;
							menu.clear();
							menu.add(menu()).expandY().fill().padLeft(5).top();
							clearView();
						}
					}
				};
			}

			private void update() {
				clear();
				defaults().uniformX().fillX().expandX();
				// sort all definitions
				sortedDefinitions.clear();
				for (Object i : rootDefinition.keys()) {
					final String idx = (String) i;
					sortedDefinitions.add(rootDefinition.get(idx));
				}
				sortedDefinitions.sort(new Comparator<Definition>() {
					@Override
					public int compare(Definition def, Definition otherDef) {
						if (def.getTag().equals(otherDef.getTag())) {
							return def.getName().compareTo(otherDef.getName());
						}
						return def.getTag().compareTo(otherDef.getTag());
					}
				});
				// display
				buttons.clear();
				String heading = null;
				for (final Definition d : sortedDefinitions) {
					if (d.getTag() != null && !d.getTag().equals(heading)) {
						heading = d.getTag();
						if (expandedTags.size() == 0) {
							expandedTags.add(heading);
						}
						add(new Table() {
							{
								add(new VisLabel(StringUtils.isNotEmpty(d.getTag()) ? d.getTag() : "Untagged")).expandX();
								add(WidgetUtils.button(expandedTags.contains(d.getTag()) ? "-" : "+", new Listener() {

									@Override
									public void invoke() {
										if (expandedTags.contains(d.getTag())) {
											expandedTags.remove(d.getTag());
										} else {
											expandedTags.add(d.getTag());
										}
										update();
									}
								})).size(15);
							}
						}).expandX().width(200).row();
					}
					if (expandedTags.contains(d.getTag())) {
						final VisTextButton button = new VisTextButton(d.getName());
						button.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								view.clear();
								create((T) d, view);
								currentIndex = d;
								updateHighlighted(button);
							}

						});
						add(button).row();
						buttons.add(button);
					}
				}
				add().expand().fill().row();
				add(WidgetUtils.button("Add Item", addListener())).row();
				add(WidgetUtils.button("Delete Item", deleteListener()));
			}
		};
	}

	@Override
	public void populate(Table content) {
		content.add(menu = menu()).expandY().fill().padLeft(5).top();
		clearView();
		content.add(view).expand().top();
	};

	@Override
	public void save() {
		ObjectMap<String, T> newObjects = new ObjectMap<>();
		if (rootDefinition.items() == null) {
			return;
		}
		ObjectMap<String, T> oldObjects = rootDefinition.items();
		for (T item : oldObjects.values()) {
			newObjects.put(item.getName(), item);
		}
		rootDefinition.setItems(newObjects);
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(rootDefinition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}

	private void updateHighlighted(VisTextButton highlightedButton) {
		if (highlightedButton == null) {
			return;
		}
		// highlight
		for (VisTextButton button : buttons) {
			button.getLabel().setColor(button == highlightedButton ? Color.YELLOW : Color.WHITE);
		}
	}

}
