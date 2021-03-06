package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationFrameDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.AnimationCollectionComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.AnimationComponentDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition.ScriptComponentDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.MapEditorWidget.EditMode;
import net.alcuria.umbracraft.util.MapUtils;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/** A representation of a single tile on the {@link MapEditorWidget} actor.
 * Handles updating the {@link MapDefinition} when it is clicked, either by
 * adjusting the altitude or adding an {@link EntityDefinition}.
 * @author Andrew Keturi */
public class MapTileWidget extends Table {

	static int selAlt = -1;
	static int selX = -1;
	static int selY = -1;
	private static TextureRegion side, top, edge, outline;
	private final MapDefinition definition;
	private Image entityPreview;
	private final int i, j;
	private final Color overlay = new Color(0, 0, 0, 0.9f);
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
				//TODO: compare old selX and intermittently call entered
				float oldSelX = selX;
				float oldSelY = selY;
				selX = i;
				selY = j;
				final float step = Math.max(Math.abs(oldSelX - selX), Math.abs(oldSelY) - selY);
				float dX = (selX - oldSelX) / step;
				float dY = (selY - oldSelY) / step;
				while ((int) oldSelX != selX && (int) oldSelY != selY) {
					oldSelX += dX;
					oldSelY += dY;
					updateTile((int) oldSelX, (int) oldSelY);
				}
				selAlt = definition.tiles.get(selX).get(selY).altitude;
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
		if (widget.getEditMode() == EditMode.ALTITUDE && widget.isEntered()) {
			updateTile(selX, selY);
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
		int type = type(i, j);
		int overlayType = overlayType(i, j);
		int y = definition.getHeight() - j - 1;
		boolean isTeleport = (i == definition.eastX && y == definition.eastY) || //
				(i == definition.westX && y == definition.westY) || //
				(i == definition.southX && y == definition.southY) || //
				(i == definition.northX && y == definition.northY);
		// side
		batch.draw(side, getX(), getY(), getWidth(), getWidth() * altitude);
		// top
		batch.draw(outline, getX(), getY() + altitude * getHeight(), getWidth(), getHeight());
		if (isTeleport) {
			batch.setColor(Color.DARK_GRAY);
		}
		if (type != 0) {
			batch.setColor(MapUtils.getTerrainColor(type));
		}
		batch.draw(top, getX() + 1, getY() + altitude * getHeight() + 1, getWidth() - 2, getHeight() - 2);
		if (overlayType != 0) {
			batch.setColor(MapUtils.getTerrainColor(overlayType));
			batch.draw(side, getX(), getY() + definition.overlayHeight * getHeight(), getWidth(), getHeight());
			batch.draw(top, getX(), getY() + definition.overlayHeight * getHeight(), getWidth(), getHeight());
		}
		if (type != 0 || overlayType != 0 || isTeleport) {
			batch.setColor(Color.WHITE);
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

		// darken bottom clamps
		if (y < definition.bottomClamp) {
			batch.setColor(overlay);
			batch.draw(top, getX(), getY(), getWidth(), getHeight());
			batch.setColor(Color.WHITE);
		}
	}

	public void drawEntity(Batch batch) {
		int altitude = alt(i, j);
		if (entityPreview != null) {
			if (entityPreview instanceof AnimationPreview && ((AnimationPreview) entityPreview).getCurrentRegion() != null) {
				final TextureRegion region = ((AnimationPreview) entityPreview).getCurrentRegion();
				final int offset = (int) ((AnimationPreview) entityPreview).getOriginX();
				batch.draw(region, widget.getActor(widget.getZoom()).getX() + getX(), widget.getActor(widget.getZoom()).getY() + (definition.getHeight() - j - 1) * Config.tileWidth * 2 / widget.getZoom() + altitude * getHeight(), region.getRegionWidth() * 2 / widget.getZoom(), region.getRegionHeight() * 2 / widget.getZoom());
			} else {
				entityPreview.setX(widget.getActor(widget.getZoom()).getX() + getX());
				entityPreview.setY(widget.getActor(widget.getZoom()).getY() + (definition.getHeight() - j - 1) * Config.tileWidth * 2 / widget.getZoom() + altitude * getHeight());
				entityPreview.setScale(2 * widget.getZoom());
				entityPreview.draw(batch, 1);
			}
		}
	}

	private int overlayType(int i, int j) {
		if (i < 0 || i >= definition.tiles.size || j < 0 || j >= definition.tiles.get(0).size) {
			return 0;
		}
		return definition.tiles.get(i).get(j).overlayType;
	}

	private int type(int i, int j) {
		if (i < 0 || i >= definition.tiles.size || j < 0 || j >= definition.tiles.get(0).size) {
			return 0;
		}
		return definition.tiles.get(i).get(j).type;
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
						if (componentDefinition instanceof ScriptComponentDefinition) {
							final ScriptComponentDefinition scriptComponentDefinition = (ScriptComponentDefinition) componentDefinition;
							final ScriptDefinition definition = Editor.db().script(scriptComponentDefinition.script);
							if (StringUtils.isNotEmpty(definition.pages.get(0).animationCollection)) {
								AnimationCollectionDefinition animDefinition = Editor.db().animCollection(definition.pages.get(0).animationCollection);
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
							} else if (StringUtils.isNotEmpty(definition.pages.get(0).animationGroup)) {
								AnimationGroupDefinition animGroup = Editor.db().animGroup(definition.pages.get(0).animationGroup);
								if (animGroup != null) {
									final AnimationDefinition anim = Editor.db().anim(animGroup.down);
									if (anim != null) {
										entityPreview = new AnimationPreview(anim);
										break;
									}
								}
							} else if (StringUtils.isNotEmpty(definition.pages.get(0).animation)) {
								AnimationDefinition anim = Editor.db().anim(definition.pages.get(0).animation);
								if (anim != null) {
									entityPreview = new AnimationPreview(anim);
									break;
								}
							}
						} else if (componentDefinition instanceof AnimationComponentDefinition) {
							// create the definition from the component
							final AnimationComponentDefinition animationComponentDefinition = (AnimationComponentDefinition) componentDefinition;
							if (StringUtils.isNotEmpty(animationComponentDefinition.animationComponent)) {
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
								if (animDefinition.template != null) {
									AnimationFrameDefinition templateFrameDef = new AnimationFrameDefinition();
									templateFrameDef.color = Color.WHITE;
									templateFrameDef.duration = 10;
									templateFrameDef.x = 1;
									templateFrameDef.y = 2;
									AnimationDefinition templateDef = new AnimationDefinition();
									templateDef.filename = animDefinition.template;
									templateDef.frames = new Array<AnimationFrameDefinition>();
									templateDef.height = Editor.db().config().templateHeight;
									templateDef.width = Editor.db().config().templateWidth;
									templateDef.keepLast = true;
									templateDef.loop = false;
									templateDef.name = "Template";
									templateDef.originX = Editor.db().config().templateWidth / 2;
									templateDef.originY = 4;
									templateDef.frames.add(templateFrameDef);
									entityPreview = new AnimationPreview(templateDef);
								} else {
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
				} else {
					entityPreview = new Image(Drawables.get("entity"));
					break;
				}
			}
		}
	}

	private void updateTile(int x, int y) {
		try {
			// set the altitude with 0 - 9 number keys
			for (int i = 7; i <= 16; i++) {
				if (Gdx.input.isKeyPressed(i)) {
					definition.tiles.get(x).get(y).altitude = i - 7;
				}
			}
			// terrain setters
			if (Gdx.input.isKeyPressed(Keys.Q)) {
				definition.tiles.get(x).get(y).type = 0;
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				definition.tiles.get(x).get(y).type = 1;
			}
			if (Gdx.input.isKeyPressed(Keys.E)) {
				definition.tiles.get(x).get(y).type = 2;
			}
			if (Gdx.input.isKeyPressed(Keys.R)) {
				definition.tiles.get(x).get(y).type = 3;
			}
			if (Gdx.input.isKeyPressed(Keys.T)) {
				definition.tiles.get(x).get(y).type = 4;
			}
			// stair setter
			if (Gdx.input.isKeyPressed(Keys.S)) {
				definition.tiles.get(x).get(y).type = 5;
			}
			// tree wall setter
			if (Gdx.input.isKeyPressed(Keys.A)) {
				if (definition.tiles.get(x).get(y).type != 6) {
					if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
						definition.tiles.get(x).get(y).altitude += 4;
					}
					definition.tiles.get(x).get(y).type = 6;
				}
			}
			// upper layer
			if (Gdx.input.isKeyPressed(Keys.Z)) {
				definition.tiles.get(x).get(y).overlayType = 0;
			}
			// overlay
			if (Gdx.input.isKeyPressed(Keys.X)) {
				definition.tiles.get(x).get(y).overlayType = 1;
			}
			// obstacle 1
			if (Gdx.input.isKeyPressed(Keys.C)) {
				definition.tiles.get(x).get(y).overlayType = 2;
			}
			// obstacle 2
			if (Gdx.input.isKeyPressed(Keys.V)) {
				definition.tiles.get(x).get(y).overlayType = 3;
			}
			// obstacle 3
			if (Gdx.input.isKeyPressed(Keys.B)) {
				definition.tiles.get(x).get(y).overlayType = 4;
			}
			// obstacle 4
			if (Gdx.input.isKeyPressed(Keys.N)) {
				definition.tiles.get(x).get(y).overlayType = 5;
			}
		} catch (Exception e) {
			System.err.println("Out of bounds " + selX + ", " + selY);
		}

	}
}
