package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class AnimationsModule extends Module<AnimationListDefinition> {

	private Table currentAnimTable, listAnimTable;

	public AnimationsModule() {
		super();
		load(AnimationListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new AnimationListDefinition();
		}
	}

	private void addAnimationList(final Table content) {

		ScrollPane scroll = new ScrollPane(new Table() {
			{
				if (rootDefinition != null && rootDefinition.animations != null) {
					for (int i = 0; i < rootDefinition.animations.size; i++) {
						final AnimationDefinition anim = rootDefinition.animations.get(i);
						final VisTextButton animButton = new VisTextButton(anim.name != null ? anim.name : "New Animation");
						animButton.addListener(new ClickListener() {

							@Override
							public void clicked(InputEvent event, float x, float y) {
								currentAnimTable.clear();
								createCurrentAnimTable(content, currentAnimTable, anim);
							}

						});
						add(animButton).row();
					}
				}
				VisTextButton addButton = new VisTextButton("Add Animation");
				addButton.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						rootDefinition.add();
						content.clear();
						populate(content);
					}
				});
				add().expandY().fill().row();
				add(addButton).padTop(20).padBottom(20).row();
			}
		});
		content.add(scroll).expandY().fill();
	}

	private void createCurrentAnimTable(final Table content, final Table table, final AnimationDefinition definition) {
		table.add(new Table() {
			{
				if (definition.filename != null) {
					add(new Image(new Texture(Gdx.files.internal("sprites/animations/" + definition.filename))));
				}
				add(new Table() {
					{
						populate(this, AnimationDefinition.class, definition);
						row();
						VisTextButton deleteButton = new VisTextButton("Delete Animation");
						deleteButton.addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								int idx = definition.getId() - 1;
								rootDefinition.delete(definition);
								content.clear();
								populate(content);
							};
						});
						add(deleteButton);
					}
				});
			}
		}).row();
		;

	}

	@Override
	public String getTitle() {
		return "Animations";
	}

	@Override
	public void populate(Table content) {
		addAnimationList(content);
		content.add(currentAnimTable = new Table() {
			{
				add(new VisLabel("Select an animation from the left", Color.RED));
			}
		}).expand().fill();
	}
}
