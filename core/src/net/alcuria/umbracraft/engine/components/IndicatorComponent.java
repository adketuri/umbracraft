package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class IndicatorComponent implements Component {

	public static enum IndicatorType {
		ITEM
	}

	private Entity entity;
	private Image icon;
	private float time = 0;
	private IndicatorType type;
	private int x, y;

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
			//			Game.batch().draw(icon, x, y, icon.getRegionWidth() / 2, icon.getRegionHeight() / 2, icon.getRegionWidth(), icon.getRegionHeight(), 1, 1, 0);
			icon.draw(Game.batch(), 1);
		}
	}

	public void start(IndicatorType type, String id) {
		this.type = type;
		switch (type) {
		case ITEM:
			icon = new Image(Drawables.skin("icons/" + id));
			break;
		default:
			break;
		}
		time = 0;
		x = (int) entity.position.x;
		y = (int) (entity.position.y + entity.position.z);
		icon.setPosition(x, y);
	}

	public void stop(IndicatorType type) {
		switch (type) {
		case ITEM:
			icon = null;
			break;
		default:
			break;
		}
	}

	@Override
	public void update(Entity entity) {
		if (icon != null) {
			x = (int) entity.position.x;
			y = (int) (entity.position.y + entity.position.z);
			icon.setPosition(x, y);
			icon.act(Gdx.graphics.getDeltaTime());
		}
	}
}
