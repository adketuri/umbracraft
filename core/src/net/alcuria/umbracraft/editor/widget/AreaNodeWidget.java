package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** Recursively displays a list of {@link AreaNodeDefinition} buttons, with
 * children below and pointers to parents/children.
 * @author Andrew Keturi */
public class AreaNodeWidget extends Table {
	public interface NodeClickHandler {
		void onClick(AreaNodeDefinition definition);
	}

	private final Array<VisTextButton> childrenButtons = new Array<VisTextButton>();
	private final AreaNodeDefinition root;
	private VisTextButton rootButton;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	public AreaNodeWidget(final AreaNodeDefinition root, final NodeClickHandler listener) {
		this.root = root;
		defaults().pad(10);
		add(rootButton = new VisTextButton(root.getName()) {
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
						final AreaNodeWidget widget = new AreaNodeWidget(child, listener);
						add(widget);
						childrenButtons.add(widget.getRootButton());
					}
				}
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (root != null && root.children != null) {
			drawConnector(batch);
		}
	}

	private void drawConnector(Batch batch) {
		batch.end();
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(0, 300, 300, 0);
		shapeRenderer.end();
		batch.begin();
	}

	protected VisTextButton getRootButton() {
		return rootButton;
	}
}
