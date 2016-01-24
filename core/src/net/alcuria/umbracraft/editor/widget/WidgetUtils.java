package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.layout.EditorLayout;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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

}
