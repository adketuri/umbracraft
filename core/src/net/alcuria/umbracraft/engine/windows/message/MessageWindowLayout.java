package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.windows.WindowLayout;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MessageWindowLayout extends WindowLayout {

	@Override
	public void create() {
		content.add(new Table() {
			{
				setBackground(Drawables.get("black"));
				add(new VisLabel("Hello")).expand().bottom();
			}
		}).expand().bottom().height(30);
	}

}
