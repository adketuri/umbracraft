package net.alcuria.umbracraft.widget;

import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** Some widget utils.
 * @author Andrew Keturi */
public class WidgetUtils {

	public static void divider(Table table, final String drawable) {
		assert (table != null);
		table.add(new Table() {
			{
				setBackground(Drawables.get(drawable));
			}
		}).expandX().fill().height(1).row();
	}

}
