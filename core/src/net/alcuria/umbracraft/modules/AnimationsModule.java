package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.widget.AnimationPreview;
import net.alcuria.umbracraft.widget.AnimationPreviewFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class AnimationsModule extends Module<AnimationListDefinition> {

	private Table currentAnimTable;

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
						final AnimationDefinition definition = rootDefinition.animations.get(i);
						final VisTextButton animButton = new VisTextButton(definition.name != null ? definition.name : "New Animation");
						animButton.addListener(new ClickListener() {

							@Override
							public void clicked(InputEvent event, float x, float y) {
								currentAnimTable.clear();
								createCurrentAnimTable(content, currentAnimTable, definition, animButton);
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

	private void createCurrentAnimTable(final Table content, final Table table, final AnimationDefinition definition, final VisTextButton button) {
		final ScrollPane scroll = new ScrollPane(createFrames(definition));

		table.add(new Table() {
			{
				final Image image = new Image();
				add(image);
				if (definition.filename != null) {
					add(new AnimationPreview(definition)).size(definition.frameWidth * 2, definition.frameHeight * 2).pad(30);
					String path = "sprites/animations/" + definition.filename;
					image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
				} else {
					add();
				}
				populate(this, AnimationDefinition.class, definition, updateButtonListener(image, button, scroll, definition), 1);
				row();
				VisTextButton deleteButton = new VisTextButton("Delete Animation");
				deleteButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						rootDefinition.delete(definition);
						content.clear();
						populate(content);
					};
				});
				add(deleteButton);
			}
		}).row();

		table.add(scroll).expandY().fill().row();
		table.add(new Table() {
			{
				final VisTextButton addFrameButton = new VisTextButton("Add Frame");
				addFrameButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						definition.frames.add(new AnimationFrameDefinition());
						scroll.clear();
						scroll.setWidget(createFrames(definition));
					}
				});
				add(addFrameButton);
				if (definition.frames != null && definition.frames.size > 0) {
					final VisTextButton removeFrameButton = new VisTextButton("Remove Frame");
					removeFrameButton.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							definition.frames.removeIndex(definition.frames.size - 1);
							scroll.clear();
							scroll.setWidget(createFrames(definition));
						}
					});
					add(removeFrameButton);
				}
			}
		});
	}

	private Table createFrames(final AnimationDefinition definition) {
		return new Table() {
			{
				if (definition.frames == null) {
					definition.frames = new Array<>();
				}
				for (int i = 0; i < definition.frames.size; i++) {
					final int idx = i;
					add(new Table() {
						{
							final AnimationFrameDefinition frame = definition.frames.get(idx);
							final AnimationPreviewFrame image = new AnimationPreviewFrame(definition, frame);
							add(image).size(definition.frameWidth * 2, definition.frameHeight * 2);
							add(new VisLabel("Frame " + (idx + 1)));
							populate(this, AnimationFrameDefinition.class, frame, new Listener() {

								@Override
								public void invoked() {
									image.update(definition, frame);
								}
							}, 3);
						}
					}).row();
				}
				add().expand().fill().row();
			}
		};
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

	/** The listener for updating anything when the {@link AnimationDefinition}
	 * changes */
	private Listener updateButtonListener(final Image image, final VisTextButton button, final ScrollPane scroll, final AnimationDefinition definition) {
		return new Listener() {

			@Override
			public void invoked() {
				String path = "sprites/animations/" + definition.filename;
				if (Gdx.files.internal(path).exists()) {
					image.setVisible(true);
					image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
				} else {
					image.setVisible(false);
				}
				scroll.clear();
				scroll.setWidget(createFrames(definition));
				button.setText(definition.name);
			}

		};
	}
}
