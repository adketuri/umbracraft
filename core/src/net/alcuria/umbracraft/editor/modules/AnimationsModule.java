package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.editor.widget.AnimationPreview;
import net.alcuria.umbracraft.editor.widget.AnimationPreviewFrame;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The module for all the animations.
 * @author Andrew Keturi */
public class AnimationsModule extends Module<AnimationListDefinition> {

	private Table currentAnimTable;
	private Table previewTable;

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
				defaults().expandX().uniformX().fill();
				if (rootDefinition != null && rootDefinition.animations != null) {
					for (final AnimationDefinition definition : rootDefinition.animations.values()) {
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
				add().expandY().fill().row();
				VisTextButton addButton = new VisTextButton("Add Animation");
				addButton.addListener(new ClickListener() {

					@Override
					public void clicked(InputEvent event, float x, float y) {
						rootDefinition.add();
						content.clear();
						populate(content);
					}
				});
				add(addButton).padTop(20).padBottom(20).row();
			}
		});
		content.add(scroll).expandY().fill().padLeft(5);
	}

	private void createCurrentAnimTable(final Table content, final Table table, final AnimationDefinition definition, final VisTextButton button) {
		final ScrollPane scroll = new ScrollPane(new Actor());
		scroll.setWidget(createFrames(scroll, definition));
		table.add(new Table() {
			{
				final Image image = new Image();
				add(image);
				add(previewTable = new Table() {
					{
						add(new AnimationPreview(definition)).size(definition.width * 2, definition.height * 2).pad(30);
					}
				});
				if (definition.filename != null) {
					String path = "sprites/animations/" + definition.filename;
					if (Gdx.files.internal(path).exists()) {
						image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
					}
				}
				populate(this, AnimationDefinition.class, definition, animationPopulateConfig(image));
				row();
				final VisTextButton deleteButton = new VisTextButton("Delete Animation");
				deleteButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						rootDefinition.delete(definition);
						content.clear();
						populate(content);
					};
				});
				final VisTextButton cloneButton = new VisTextButton("Clone Animation");
				cloneButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						rootDefinition.clone(definition);
						content.clear();
						populate(content);
					};
				});
				add(new Table() {
					{
						add(cloneButton).row();
						add(deleteButton);
					}
				});

			}

			private PopulateConfig animationPopulateConfig(Image image) {
				PopulateConfig cfg = new PopulateConfig();
				cfg.listener = updateButtonListener(image, button, scroll, definition);
				cfg.cols = 1;
				cfg.labelWidth = 80;
				cfg.textFieldWidth = 200;
				return cfg;
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
						scroll.setWidget(createFrames(scroll, definition));
						updatePreviewAnimation(definition);
					}
				});
				add(addFrameButton).padBottom(20);
			}
		});
	}

	private Table createFrames(final ScrollPane scroll, final AnimationDefinition definition) {
		return new Table() {
			{
				if (definition.frames == null) {
					definition.frames = new Array<AnimationFrameDefinition>();
				}
				for (int i = 0; i < definition.frames.size; i++) {
					final int idx = i;
					if (idx != 0) {
						WidgetUtils.divider(this, "blue");
					}
					add(new Table() {
						{
							defaults().pad(5, 20, 5, 20);
							final AnimationFrameDefinition frame = definition.frames.get(idx);
							final AnimationPreviewFrame image = new AnimationPreviewFrame(definition, frame);
							add(image).size(definition.width * 2, definition.height * 2);
							add(new VisLabel("Frame " + (idx + 1)));
							populate(this, AnimationFrameDefinition.class, frame, frameConfig(image, frame));
							add(new VisTextButton("Delete") {
								{
									addListener(new ClickListener() {
										@Override
										public void clicked(InputEvent event, float x, float y) {
											if (idx < definition.frames.size && idx >= 0) {
												definition.frames.removeIndex(idx);
											}
											scroll.clear();
											scroll.setWidget(createFrames(scroll, definition));
										};
									});
								}
							});
							add(new VisTextButton("Clone") {
								{
									addListener(new ClickListener() {
										@Override
										public void clicked(InputEvent event, float x, float y) {
											if (idx < definition.frames.size && idx >= 0) {
												definition.frames.insert(idx, definition.frames.get(idx).copy());
											}
											scroll.clear();
											scroll.setWidget(createFrames(scroll, definition));
										};
									});
								}
							});

						}

						private PopulateConfig frameConfig(final AnimationPreviewFrame image, final AnimationFrameDefinition frame) {
							PopulateConfig cfg = new PopulateConfig();
							cfg.listener = new Listener() {

								@Override
								public void invoke() {
									image.update(definition, frame);
								}
							};
							cfg.cols = 4;
							cfg.labelWidth = 30;
							cfg.textFieldWidth = 30;
							return cfg;
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

	@Override
	public void save() {
		ObjectMap<String, AnimationDefinition> newObjects = new ObjectMap<>();
		if (rootDefinition.animations == null) {
			return;
		}
		ObjectMap<String, AnimationDefinition> oldObjects = rootDefinition.animations;
		for (AnimationDefinition item : oldObjects.values()) {
			newObjects.put(item.getName(), item);
		}
		rootDefinition.animations = newObjects;
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(rootDefinition);
		Gdx.files.external("umbracraft/" + getTitle().toLowerCase() + ".json").writeString(jsonStr, false);
	}

	/** The listener for updating anything when the {@link AnimationDefinition}
	 * changes */
	private Listener updateButtonListener(final Image image, final VisTextButton button, final ScrollPane scroll, final AnimationDefinition definition) {
		return new Listener() {

			@Override
			public void invoke() {
				String path = "sprites/animations/" + definition.filename;
				if (definition.filename != null && definition.filename.length() > 0 && Gdx.files.internal(path).exists()) {
					image.setVisible(true);
					image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
				} else {
					image.setVisible(false);
				}
				scroll.clear();
				scroll.setWidget(createFrames(scroll, definition));
				updatePreviewAnimation(definition);
				button.setText(definition.name);
			}

		};
	}

	private void updatePreviewAnimation(AnimationDefinition definition) {
		if (definition == null || previewTable == null) {
			return;
		}
		previewTable.clear();
		previewTable.add(new AnimationPreview(definition)).size(definition.width * 2, definition.height * 2).pad(30);
	}
}
