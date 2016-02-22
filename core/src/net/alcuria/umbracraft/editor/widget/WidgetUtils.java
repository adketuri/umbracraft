package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.events.HideTooltip;
import net.alcuria.umbracraft.editor.events.ShowTooltip;
import net.alcuria.umbracraft.editor.layout.EditorLayout;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** Some utility functions to display recurring UI elements in the
 * {@link EditorLayout}.
 * @author Andrew Keturi */
public class WidgetUtils {

	/** A convenience function that returns a button with an action
	 * @param text a {@link String} to display on the button
	 * @param listener the {@link Listener} to invoke on click
	 * @return the {@link VisTextButton} */
	public static TextButton button(final String text, final Listener listener) {
		return new VisTextButton(text) {
			{
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						listener.invoke();
					};
				});
			}
		};
	}

	/** Adds a divider to a table
	 * @param table the {@link Table} to add the divider to
	 * @param drawable the {@link Drawable} string, eg "yellow" or "blue" */
	public static void divider(Table table, final String drawable) {
		assert (table != null);
		table.add(new Table() {
			{
				setBackground(Drawables.get(drawable));
			}
		}).expandX().fill().height(1).row();
	}

	/** Adds a modifiable list to the content table, with options to delete.
	 * @param content the table to add the widget to
	 * @param items a reference to the items array
	 * @param suggestions all the suggestions for input */
	public static void modifiableList(final Table content, final Array<String> items, final Array<String> suggestions) {
		content.add(new Table() {
			{
				refresh();
			}

			private void refresh() {
				clear();
				for (int i = 0; i < items.size; i++) {
					add(new VisLabel(items.get(i)));
					final int idx = i;
					add(button("x", new Listener() {
						@Override
						public void invoke() {
							items.removeIndex(idx);
							refresh();
						}
					})).row();
				}
				final SuggestionWidget textField = new SuggestionWidget(suggestions, 100);
				add(textField.getActor());
				textField.setGenericPopulate(null);
				add(button("+", new Listener() {

					@Override
					public void invoke() {
						final String text = textField.getTextField().getText();
						if (StringUtils.isNotEmpty(text)) {
							items.add(text);
							refresh();
						}
					}
				}));
			}
		});
	}

	public static Actor pad(final Actor actor, final int padding) {
		return new Table() {
			{
				add(actor).pad(padding);
			}
		};
	}

	/** Adds a popup-style title to a table
	 * @param table the {@link Table} that gets the popup title
	 * @param title a {@link String} to display on the title
	 * @param closeListener the {@link Listener} to invoke when the close button
	 *        is pressed */
	public static void popupTitle(final Table table, final String title, final Listener closeListener) {
		table.add(new Table() {
			{
				setBackground(Drawables.get("blue"));
				add(new VisLabel(title)).expand().center();
				add(new VisTextButton("X") {
					{
						addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								if (closeListener != null) {
									closeListener.invoke();
								}
							};
						});
					}
				});
			}
		}).expand().fillX().top().row();
	}

	/** Creates a tooltip with the given {@link String} value on hover. Note,
	 * this only does things in the {@link Editor}.
	 * @param value a {@link String} to display on hover
	 * @return a tooltip {@link Actor} */
	public static Actor tooltip(final String value) {
		return new Table() {
			{
				final Vector2 tmp = new Vector2();
				final VisLabel helpLabel = new VisLabel("[?]", Color.YELLOW);
				add(helpLabel).pad(5);
				helpLabel.addListener(new ClickListener() {
					@Override
					public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
						tmp.x = -160;
						tmp.y = -30;
						Editor.publisher().publish(new ShowTooltip(helpLabel.localToStageCoordinates(tmp), value));
					};

					@Override
					public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
						Editor.publisher().publish(new HideTooltip());
					};
				});
			}
		};
	};

}
