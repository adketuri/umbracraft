package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class PhysicsComponent implements BaseComponent {

	private final Map map;
	Vector2 tmp;

	public PhysicsComponent(Map map) {
		this.map = map;
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
		if (object.desiredPosition.dst2(object.position) > 3) {
			// simple move towards the desired position
			tmp.set(object.position);
			object.velocity.set(tmp.sub(object.desiredPosition).nor().rotate(180).scl(150 * Gdx.graphics.getDeltaTime()));
		} else {
			object.velocity.set(Vector2.Zero);
		}
		object.position.add(object.velocity);
	}
}
