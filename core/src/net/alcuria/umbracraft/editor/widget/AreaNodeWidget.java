package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** Recursively displays a list of {@link AreaNodeDefinition} buttons, with
 * children below and pointers to parents/children.
 * @author Andrew Keturi */
public class AreaNodeWidget extends Table {

	public interface NodeClickHandler {
		void onClick(AreaNodeDefinition definition);
	}

	final AreaNodeDefinition root;

	public AreaNodeWidget(final AreaNodeDefinition root, final NodeClickHandler listener) {
		this.root = root;
		defaults().pad(10);
		add(new VisTextButton(root.getName()) {
			{
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						listener.onClick(root);
					};
				});
			}
		}).row();
		add(new Table() {
			{
				if (root.children != null) {
					for (AreaNodeDefinition child : root.children) {
						add(new AreaNodeWidget(child, listener));
					}
				}
			}
		});
	}
}
