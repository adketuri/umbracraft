package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** Recursively displays a list of {@link AreaNodeDefinition} buttons, with
 * children below and pointers to parents/children.
 * @author Andrew Keturi */
public class AreaNodeWidget {

	AreaNodeDefinition root;

	public AreaNodeWidget(AreaNodeDefinition root) {
		this.root = root;
	}

	public Table getWidget() {
		return new Table() {
			{
				defaults().pad(10);
				add(new VisTextButton(root.getName())).row();
				add(new Table() {
					{
						if (root.children != null) {
							for (AreaNodeDefinition child : root.children) {
								add(new AreaNodeWidget(child).getWidget());
							}
						}
					}
				});
			}
		};
	}

}
