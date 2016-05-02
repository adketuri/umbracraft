package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
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
		if (type != null && icon != null) {
			icon.draw(Game.batch(), 1);
		}
	}

	public void start(IndicatorType type, String id) {
		this.type = type;
		switch (type) {
		case ITEM:
			id = StringUtils.replaceArgs(id, entity.getArguments());
			ItemDefinition item = Game.db().item(id);
			icon = new Image(Drawables.skin("icons/" + item.icon));
			updatePos(pos);
			icon.setPosition(pos.x, pos.y);
			icon.setOrigin(icon.getWidth() / 2, icon.getHeight() / 2);
			icon.addAction(Actions.sequence(Actions.parallel(Actions.sequence(Actions.scaleTo(1.4f, 1.4f, 0.3f, Interpolation.pow2Out), Actions.scaleTo(1f, 1f, 0.3f, Interpolation.pow2In)), Actions.moveBy(0, 45, 0.5f, Interpolation.pow2Out)), Actions.forever(Actions.sequence(Actions.moveBy(0, -10, 0.3f, Interpolation.sineOut), Actions.moveBy(0, 10, 0.3f, Interpolation.sineOut)))));
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
