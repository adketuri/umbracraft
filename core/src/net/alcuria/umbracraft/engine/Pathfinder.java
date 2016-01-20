package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/** Handles pathfinding through a {@link DirectedInputComponent}
 * @author Andrew Keturi */
public class Pathfinder extends Thread {

	public static class PathNode {
		public PathNode parent;
		public int x, y, f, g, h;

		public PathNode(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void draw(Color color, int altitude, SpriteBatch batch) {
			batch.setColor(color);
			final int w = Config.tileWidth;
			//			Game.log(color + " " + x + " " + y);
			batch.draw(Game.assets().get("debug.png", Texture.class), x * w, y * w + altitude * w, w, w);
			batch.setColor(Color.WHITE);
		}

		@Override
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	}

	private final Array<PathNode> closed = new Array<PathNode>();
	private final DirectedInputComponent component;
	private PathNode destination, source;
	private final Array<PathNode> open = new Array<PathNode>();

	public Pathfinder(DirectedInputComponent component) {
		this.component = component;
	}

	public boolean isRunning() {
		return source != null && destination != null && source.x != destination.x && source.y != destination.y;
	}

	public void renderPaths() {
		for (PathNode n : open) {
			n.draw(Color.GREEN, Game.map().getAltitudeAt(n.x, n.y), Game.batch());
		}
		for (PathNode n : closed) {
			n.draw(Color.RED, Game.map().getAltitudeAt(n.x, n.y), Game.batch());
		}
		if (source != null) {
			source.draw(Color.YELLOW, Game.map().getAltitudeAt(source.x, source.y), Game.batch());
		}
		if (destination != null) {
			destination.draw(Color.MAGENTA, Game.map().getAltitudeAt(destination.x, destination.y), Game.batch());
		}
	}

	@Override
	public void run() {
		while (isRunning()) {

		}
	}

	public void setDestination(PathNode source, PathNode destination) {
		if (source == null) {
			throw new NullPointerException("source cannot be null");
		}
		if (destination == null) {
			throw new NullPointerException("destination cannot be null");
		}
		Game.log("Setting source: " + source + " dest: " + destination);
		// clear out the lists
		open.clear();
		closed.clear();
		// set source and dest
		this.source = source;
		this.destination = destination;
		setName(String.format("(%d, %d) -> (%d, %d)", source.x, source.y, destination.x, destination.y));
		if (!isRunning()) {
			start();
		}
	}

	public void update(Entity entity) {
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			setDestination(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode(0, 0));
		}
	}

}
