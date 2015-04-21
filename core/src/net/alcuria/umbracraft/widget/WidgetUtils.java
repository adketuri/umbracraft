package net.alcuria.umbracraft.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class WidgetUtils {

	public static void divider(Table table) {
		table.add(new Table() {
			{
				setColor(Color.WHITE);
			}
		}).expandX().fill().height(3).row();
	}
}
