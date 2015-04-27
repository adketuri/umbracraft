package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObject;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PhysicsComponent implements BaseComponent {

	private final Rectangle bounds;
	private final int height = 16, width = 16; //FIXME: don't hardcode
	private final Map map;
	Vector2 tmp;

	public PhysicsComponent(Map map) {
		this.map = map;
		bounds = new Rectangle(0, 0, width, height);
	}

	@Override
	public void create() {
		tmp = new Vector2();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(GameObject object) {
	}

	@Override
	public void update(GameObject object) {
		/*
		 * if (object.desiredPosition.dst2(object.position) > 3) { // simple
		 * move towards the desired position tmp.set(object.position);
		 * object.velocity
		 * .set(tmp.sub(object.desiredPosition).nor().rotate(180).scl(150 *
		 * Gdx.graphics.getDeltaTime())); } else {
		 * object.velocity.set(Vector2.Zero); }
		 * Game.log(object.velocity.toString());
		 */
		object.position.add(object.velocity);
		// check for collisions
		bounds.x = object.position.x;
		bounds.y = object.position.y;
		if (object.velocity.y > 0) {
			// NORTH
			bounds.y += height;
		} else if (object.velocity.y < 0) {
			// SOUTH
		}
	}
}
