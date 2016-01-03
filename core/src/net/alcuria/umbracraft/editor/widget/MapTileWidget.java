package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.AnimationCollectionComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.AnimationComponentDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.MapEditorWidget.EditMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** A representation of a single tile on the {@link MapEditorWidget} actor.
 * Handles updating the {@link MapDefinition} when it is clicked, either by
 * adjusting the altitude or adding an {@link EntityDefinition}.
 * @author Andrew Keturi */
public class MapTileWidget extends Table {

	private static int selX, selY;
	private static TextureRegion side, top, edge, outline;
	private final MapDefinition definition;
	private AnimationPreview entityPreview;
	private final int i, j;
	private final MapEditorWidget widget;

	public MapTileWidget(int x, int y, final MapDefinition definition, final MapEditorWidget widget) {
		i = x;
		j = y;
		this.widget = widget;
		this.definition = definition;
		// initialize static textures if needed
		if (side == null) {
			Texture skin = new Texture(Gdx.files.internal("editor/skin.png"));
			side = new TextureRegion(skin, 4, 0, 1, 1);
			outline = new TextureRegion(skin, 5, 0, 1, 1);
			edge = new TextureRegion(skin, 2, 0, 1, 1);
			top = new TextureRegion(skin, 3, 0, 1, 1);
		}
		updateEntityPreview();
		setBackground(Drawables.get("blue"));
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (widget.getEditMode() == EditMode.ALTITUDE) {
					// adjust altitude
					if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
						MapTileWidget.this.definition.tiles.get(i).get(j).altitude--;
					} else {
						MapTileWidget.this.definition.tiles.get(i).get(j).altitude++;
					}
					MapTileWidget.this.definition.tiles.get(i).get(j).altitude = MathUtils.clamp(MapTileWidget.this.definition.tiles.get(i).get(j).altitude, 0, 10);
				} else {
					// add/edit an entity
					widget.showEntityPopup(i, definition.getHeight() - j - 1);
				}
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				setBackground(Drawables.get("yellow"));
				getColor().a = 0.5f;
				selX = i;
				selY = j;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				setBackground(Drawables.get("blue"));
				getColor().a = 1f;
			}
		});
		setTouchable(Touchable.enabled);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (entityPreview != null) {
			entityPreview.act(delta);
		}
		// set the altitude with 0 - 9 number keys
		if (widget.getEditMode() == EditMode.ALTITUDE) {
			for (int i = 7; i <= 16; i++) {
				if (Gdx.input.isKeyPressed(i)) {
					definition.tiles.get(selX).get(selY).altitude = i - 7;
				}
			}
		}
	}

	private int alt(int i, int j) {
		if (i < 0 || i >= definition.tiles.size || j < 0 || j >= definition.tiles.get(0).size) {
			return 0;
		}
		return definition.tiles.get(i).get(j).altitude;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		int altitude = alt(i, j);
		// side
		batch.draw(side, getX(), getY(), getWidth(), getWidth() * altitude);
		// top
		batch.draw(outline, getX(), getY() + altitude * getHeight(), getWidth(), getHeight());
		batch.draw(top, getX() + 1, getY() + altitude * getHeight() + 1, getWidth() - 2, getHeight() - 2);
		// entity
		if (entityPreview != null && entityPreview.getCurrentRegion() != null) {
			final TextureRegion region = entityPreview.getCurrentRegion();
			batch.draw(region, getX(), getY() + altitude * getHeight(), region.getRegionWidth() * 2, region.getRegionHeight() * 2);
		}
		// left edge
		if (alt(i - 1, j) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + altitude * getHeight(), 2, getHeight());
		}
		// right edge
		if (alt(i + 1, j) < alt(i, j)) {
			batch.draw(edge, getX() + getWidth() - 2, getY() + altitude * getHeight(), 2, getHeight());
		}
		// bottom edge
		if (alt(i, j + 1) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + altitude * getHeight(), getWidth(), 2);
		}
		// top edge
		if (alt(i, j - 1) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + getHeight() + altitude * getHeight() - 2, getWidth(), 2);
		}
	}

	/** This monolithic function is a nightmare but for now it works. It looks
	 * through the map's entity references, and if it finds an entity at this
	 * tile, it sees if that entity has an animation or something to render. If
	 * it has something to render, we initialize entityPreview and the map
	 * editor gets a nice indicator. */
	public void updateEntityPreview() {
		// set a preview image if applicable
		// first iterate thru the references on this map
		for (EntityReferenceDefinition reference : definition.entities) {
			// checking if the reference's coordinates match this tile coordinate
			if (reference.name != null && reference.x == i && definition.getHeight() - reference.y - 1 == j) {
				// verify this entity exists in the db
				EntityDefinition entity = Editor.db().entity(reference.name);
				if (entity != null) {
					// find an animation component for the preview image
					for (ComponentDefinition componentDefinition : entity.components) {
						if (componentDefinition instanceof AnimationComponentDefinition) {
							// create the definition from the component
							final AnimationComponentDefinition animationComponentDefinition = (AnimationComponentDefinition) componentDefinition;
							if (animationComponentDefinition.animationComponent != null) {
								AnimationDefinition animDefinition = Editor.db().anim(animationComponentDefinition.animationComponent);
								if (animDefinition != null) {
									entityPreview = new AnimationPreview(animDefinition);
									break;
								}
							}
						} else if (componentDefinition instanceof AnimationCollectionComponentDefinition) {
							// create the definition from the component
							AnimationCollectionDefinition animDefinition = Editor.db().animCollection(((AnimationCollectionComponentDefinition) componentDefinition).animationCollectionComponent);
							if (animDefinition != null) {
								final AnimationGroupDefinition animGroup = Editor.db().animGroup(animDefinition.idle);
								if (animGroup != null) {
									final AnimationDefinition anim = Editor.db().anim(animGroup.down);
									if (anim != null) {
										entityPreview = new AnimationPreview(anim);
										break;
									}
								}
							}
						}
					}
				}
				break;
			}
		}
	}
}
