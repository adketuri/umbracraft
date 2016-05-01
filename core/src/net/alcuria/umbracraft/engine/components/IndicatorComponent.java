package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class IndicatorComponent implements Component {

	public static enum IndicatorType {
		ITEM
	}

	private final Vector2 delta = new Vector2();
	private Entity entity;
	private Image icon;
	private final Vector2 pos = new Vector2();
	private IndicatorType type;

	@Override
	public void create(Entity entity) {
		this.entity = entity;
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {
		if (type != null) {
			icon.draw(Game.batch(), 1);
		}
	}

	public void start(IndicatorType type, String id) {
		this.type = type;
		switch (type) {
		case ITEM:
			icon = new Image(Drawables.skin("icons/" + id));
			updatePos(pos);
			icon.setPosition(pos.x, pos.y);
			icon.addAction(Actions.forever(Actions.sequence(Actions.moveBy(0, -20, 1), Actions.moveBy(0, 20, 1))));
			break;
		default:
			break;
		}
	}

	public void stop(IndicatorType type) {
		switch (type) {
		case ITEM:
			icon.clearActions();
			icon = null;
			break;
		default:
			break;
		}
	}

	@Override
	public void update(Entity entity) {
		if (icon != null) {
			updatePos(delta);
			delta.sub(pos);
			icon.moveBy(delta.x, delta.y);
			icon.act(Gdx.graphics.getDeltaTime());
			updatePos(pos);
		}
	}

	private void updatePos(Vector2 vec) {
		vec.x = (entity.position.x - icon.getWidth() / 2);
		vec.y = (entity.position.y + entity.position.z - icon.getHeight() / 2);
	}
}
