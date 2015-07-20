package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class AreaListModule extends ListModule<AreaDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new AreaDefinition());
	}

	@Override
	public void create(AreaDefinition definition, Table content) {
		if (definition.root == null) {
			definition.root = new AreaNodeDefinition();
			definition.root.name = "Root";
			definition.root.children = new Array<AreaNodeDefinition>() {
				{
					add(new AreaNodeDefinition() {
						{
							name = "Child 1";
							children = new Array<AreaNodeDefinition>() {
								{
									add(new AreaNodeDefinition() {
										{
											name = "Child 1";
											children = new Array<AreaNodeDefinition>() {
												{
													add(new AreaNodeDefinition() {
														{
															name = "Child 1";
															children = new Array<AreaNodeDefinition>() {
																{
																}
															};
														}
													});
													add(new AreaNodeDefinition() {
														{
															name = "Child 2";
														}
													});
												}
											};
										}
									});
									add(new AreaNodeDefinition() {
										{
											name = "Child 2";
										}
									});
								}
							};
						}
					});
					add(new AreaNodeDefinition() {
						{
							name = "Child 2";
						}
					});
				}
			};
		}
		AreaNodeWidget area = new AreaNodeWidget(definition.root);
		content.add(area.getWidget()).expand().fill();
	}

	@Override
	public String getTitle() {
		return "Areas";
	}

}
