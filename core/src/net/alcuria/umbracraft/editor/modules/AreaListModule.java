package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget.NodeClickHandler;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class AreaListModule extends ListModule<AreaDefinition> implements NodeClickHandler {

	private Table popupTable;

	@Override
	public void addListItem() {
		rootDefinition.add(new AreaDefinition());
	}

	@Override
	public void create(AreaDefinition definition, Table content) {
		if (definition.root == null) {
			definition.root = new AreaNodeDefinition();
			definition.root.name = "Root";
			/*
			 * definition.root.children = new Array<AreaNodeDefinition>() { {
			 * add(new AreaNodeDefinition() { { name = "Child 1"; children = new
			 * Array<AreaNodeDefinition>() { { add(new AreaNodeDefinition() { {
			 * name = "Child 1"; children = new Array<AreaNodeDefinition>() { {
			 * add(new AreaNodeDefinition() { { name = "Child 1"; children = new
			 * Array<AreaNodeDefinition>() { { } }; } }); add(new
			 * AreaNodeDefinition() { { name = "Child 2"; } }); } }; } });
			 * add(new AreaNodeDefinition() { { name = "Child 2"; } }); } }; }
			 * }); add(new AreaNodeDefinition() { { name = "Child 2"; } }); } };
			 */
		}
		AreaNodeWidget area = new AreaNodeWidget(definition.root, this);
		content.stack(area, popupTable = new Table()).expand().fill();
	}

	@Override
	public String getTitle() {
		return "Areas";
	}

	@Override
	public void onClick(final AreaNodeDefinition definition) {
		popupTable.clear();
		popupTable.add(new Table() {
			{
				setBackground(Drawables.get("black"));
				add(new Table() {
					{
						setBackground(Drawables.get("blue"));
						add(new VisTextButton("X") {
							{
								addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										popupTable.clear();
									};
								});
							}
						});
						add(new VisLabel("Editing " + definition.getName())).expand().center();
					}
				}).expand().fillX().top().row();
				add(new Table() {
					{
						PopulateConfig config = new PopulateConfig();
						config.cols = 2;
						populate(this, AreaNodeDefinition.class, definition, config);
					}
				}).expand().fill();
			}
		}).expand().fill().size(500, 500);
	}

}
