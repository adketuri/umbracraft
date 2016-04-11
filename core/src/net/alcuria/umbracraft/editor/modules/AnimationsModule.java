package net.alcuria.umbracraft.editor.modules;

import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.AnimationPreview;
import net.alcuria.umbracraft.editor.widget.AnimationPreviewFrame;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;
import net.alcuria.umbracraft.util.FileUtils;

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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;

/** The module for all the animations.
 * @author Andrew Keturi */
public class AnimationsModule extends Module<AnimationListDefinition> {

	private final Table animationListContents = new Table();
	private final Array<VisTextButton> buttons = new Array<VisTextButton>();
	private final ColorPicker colorPicker = new ColorPicker();
	private Table currentAnimTable;
	private final Set<String> expandedTags = new HashSet<String>();
	private AnimationFrameDefinition frameColorChanging;
	private String highlightedButton;
	private Table previewTable, colorPickerTable;
	private final Table scroll = new Table();
	private final Array<AnimationDefinition> sortedDefinitions = new Array<AnimationDefinition>();

	public AnimationsModule() {
		super();
		load(AnimationListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new AnimationListDefinition();
		}
		colorPicker.setListener(new ColorPickerListener() {

			@Override
			public void canceled() {

			}

			@Override
			public void finished(Color newColor) {
				if (frameColorChanging != null) {
					frameColorChanging.color = newColor;
				}
			}
		});
	}

	private void addAnimationList(final Table content) {
		buttons.clear();
		ScrollPane scroll = new ScrollPane(animationListContents);
		updateAnimationList(content);
		content.add(scroll).expandY().fill().padLeft(5);
	}

	private void createCurrentAnimTable(final Table content, final Table table, final AnimationDefinition definition, final VisTextButton button) {
		table.add(new Table() {
			{
				final Image image = new Image();
				add(image);
				add(previewTable = new Table() {
					{
						add(new AnimationPreview(definition)).size(definition.width * 2, definition.height * 2).pad(30);
					}
				});
				if (definition.filename != null && definition.filename.length() > 0) {
					String path = "sprites/animations/" + definition.filename + ".png";
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
				cfg.suggestions = new ObjectMap<String, Array<String>>();
				cfg.suggestions.put("tag", new Array<String>() {
					{
						for (AnimationDefinition def : rootDefinition.animations.values()) {
							if (!contains(def.tag, false)) {
								add(def.tag);
							}
						}
					}
				});
				cfg.suggestions.put("filename", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().spritePath, true));
				cfg.labelWidth = 80;
				cfg.textFieldWidth = 200;
				return cfg;
			}
		}).row();
		scroll.clear();
		scroll.add(createFrames(scroll, definition));
		table.add(scroll).row();
		table.add(new Table() {
			{
				final VisTextButton addFrameButton = new VisTextButton("Add Frame");
				addFrameButton.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						definition.frames.add(new AnimationFrameDefinition());
						scroll.clear();
						scroll.add(createFrames(scroll, definition));
						updatePreviewAnimation(definition);
					}
				});
				add(addFrameButton).padBottom(20);
			}
		}).expandY().top();
	}

	private Table createFrames(final Table scroll, final AnimationDefinition definition) {
		return new Table() {
			{
				stack(new Table() {
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
									final AnimationPreviewFrame image = new AnimationPreviewFrame(definition, frame) {
										@Override
										public void act(float delta) {
											super.act(delta);
											setColor(frame.color != null ? frame.color : Color.WHITE);
										}
									};
									add(image).size(definition.width * 2, definition.height * 2);
									add(new VisLabel("Frame " + (idx + 1)));
									populate(this, AnimationFrameDefinition.class, frame, frameConfig(image, frame));
									add(WidgetUtils.button("Color", new Listener() {

										@Override
										public void invoke() {
											frameColorChanging = frame;
											colorPickerTable.clear();
											colorPickerTable.add(colorPicker);
											colorPicker.fadeIn();
											colorPicker.setColor(frame.color != null ? frame.color : Color.WHITE);
										}
									}));
									add(new VisTextButton("Delete") {
										{
											addListener(new ClickListener() {
												@Override
												public void clicked(InputEvent event, float x, float y) {
													if (idx < definition.frames.size && idx >= 0) {
														definition.frames.removeIndex(idx);
													}
													scroll.clear();
													scroll.add(createFrames(scroll, definition));

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
													scroll.add(createFrames(scroll, definition));
												};
											});
										}
									});

								}

								private PopulateConfig frameConfig(final AnimationPreviewFrame image, final AnimationFrameDefinition frame) {
									PopulateConfig cfg = new PopulateConfig();
									cfg.listener = new TypeListener<String>() {

										@Override
										public void invoke(String type) {
											image.update(definition, frame);
										}
									};
									cfg.cols = 3;
									cfg.labelWidth = 30;
									cfg.textFieldWidth = 30;
									cfg.suggestions = new ObjectMap<String, Array<String>>();
									cfg.suggestions.put("particle", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().particlePath, false));
									return cfg;
								}
							}).row();
						}
					}
				}, colorPickerTable = new Table());

			}
		};
	}

	@Override
	public String getTitle() {
		return "Animations";
	}

	private Listener onExpandCollapse(final Table content, final String tag) {
		return new Listener() {

			@Override
			public void invoke() {
				if (expandedTags.contains(tag)) {
					expandedTags.remove(tag);
				} else {
					expandedTags.add(tag);
				}
				updateAnimationList(content);
			}
		};
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

	private void updateAnimationList(final Table content) {
		animationListContents.clear();
		animationListContents.add(new Table() {
			{
				defaults().expandX().uniformX().fill();
				if (rootDefinition != null && rootDefinition.animations != null) {
					// add all definitions to a new list
					sortedDefinitions.clear();
					for (final AnimationDefinition definition : rootDefinition.animations.values()) {
						sortedDefinitions.add(definition);
					}
					// sort the list
					sortedDefinitions.sort();
					// now display the list
					String heading = null;
					for (final AnimationDefinition definition : sortedDefinitions) {
						if (definition.tag != null && !definition.tag.equals(heading)) {
							heading = definition.tag;
							add(new Table() {
								{
									add(new VisLabel(definition.tag)).expandX();
									add(WidgetUtils.button(expandedTags.contains(definition.tag) ? "-" : "+", onExpandCollapse(content, definition.tag))).size(15);
								}
							}).expandX().width(200).row();
						}
						if (expandedTags.contains(definition.tag)) {
							final VisTextButton animButton = new VisTextButton(definition.name != null ? definition.name : "New Animation");
							animButton.addListener(new ClickListener() {

								@Override
								public void clicked(InputEvent event, float x, float y) {
									currentAnimTable.clear();
									createCurrentAnimTable(content, currentAnimTable, definition, animButton);
									highlightedButton = definition.name;
									updateHighlighted();
								}

							});
							add(animButton).row();
							buttons.add(animButton);
						}
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
		}).expandY().fillY();
		updateHighlighted();
	}

	/** The listener for updating anything when the {@link AnimationDefinition}
	 * changes */
	private TypeListener<String> updateButtonListener(final Image image, final VisTextButton button, final Table scroll, final AnimationDefinition definition) {
		return new TypeListener<String>() {

			@Override
			public void invoke(String type) {
				String path = "sprites/animations/" + definition.filename + ".png";
				if (definition.filename != null && definition.filename.length() > 0 && Gdx.files.internal(path).exists()) {
					image.setVisible(true);
					image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(path)))));
				} else {
					image.setVisible(false);
				}
				scroll.clear();
				scroll.add(createFrames(scroll, definition));
				updatePreviewAnimation(definition);
				button.setText(definition.name);
			}

		};
	}

	private void updateHighlighted() {
		if (highlightedButton == null) {
			return;
		}
		// highlight
		for (VisTextButton a : buttons) {
			String text = a.getLabel().getText().toString();
			a.getLabel().setColor(highlightedButton.equals(text) ? Color.YELLOW : Color.WHITE);
		}
	}

	private void updatePreviewAnimation(AnimationDefinition definition) {
		if (definition == null || previewTable == null) {
			return;
		}
		previewTable.clear();
		previewTable.add(new AnimationPreview(definition)).size(definition.width * 2, definition.height * 2).pad(30);
	}
}
