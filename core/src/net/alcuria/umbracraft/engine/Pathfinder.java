package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/** Handles pathfinding through a {@link DirectedInputComponent}
 * @author Andrew Keturi */
public class Pathfinder extends Thread {

	public static class PathNode implements Comparable<PathNode> {
		public PathNode parent;
		/* g = dist from start, h=dist from end, f= g+h */
		public int x, y, f, g, h;

		public PathNode(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(PathNode other) {
			return f - other.f;
		}

		public void draw(Color color, int altitude, SpriteBatch batch) {
			batch.setColor(color);
			final int w = Config.tileWidth;
			//			Game.log(color + " " + x + " " + y);
			batch.draw(Game.assets().get("debug.png", Texture.class), x * w, y * w + altitude * w, w, w);
			batch.setColor(Color.WHITE);
		}

		/** @param other another {@link PathNode}
		 * @return <code>true</code> if the two nodes are pointing to the same
		 *         location */
		public boolean hasSameLocationAs(final PathNode other) {
			return other.x == x && other.y == y;
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
		return source != null && destination != null && (source.x != destination.x || source.y != destination.y);
	}

	/** Searches a list for a pathNode that matches the given x,y coordinates
	 * @param list a list of {@link PathNode} objects
	 * @param x the tile x coords
	 * @param y the tile y coords
	 * @return */
	private boolean listContains(Array<PathNode> list, int x, int y) {
		if (list == null) {
			throw new NullPointerException("List cannot be null");
		}
		for (int i = 0; i < list.size; i++) {
			if (list.get(i).x == x && list.get(i).y == y) {
				return true;
			}
		}
		return false;
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
		final Map map = Game.map();
		final int[] dX = { 0, 0, 1, -1 };
		final int[] dY = { 1, -1, 0, 0 };
		open.add(source);
		while (isRunning()) {
			// get a pointer to the node in the open list with the lowest f cost
			open.sort();
			PathNode cur = open.removeIndex(0);
			closed.add(cur);

			if (cur.hasSameLocationAs(destination)) {
				Game.log("Path found!");
				return;
			}

			// foreach current node neighbor
			for (int i = 0; i < dX.length; i++) {
				// if neighbor is not traversible or neighbor is in closed, skip it
				if (!map.isInBounds(cur.x + dX[i], cur.y + dY[i]) || map.getAltitudeAt(cur.x + dX[i], cur.y + dY[i]) > map.getAltitudeAt(cur.x, cur.y) || listContains(closed, cur.x + dX[i], cur.y + dY[i])) {
					continue;
				}
				PathNode neighbor = new PathNode(cur.x + dX[i], cur.y + dY[i]);
				//	if new path to neighbor is shorter or not in open
				if (neighbor.f < cur.f || !listContains(open, neighbor.x, neighbor.y)) {
					// set f cost of nbr
					neighbor.f = Heuristic.calculateFCost(source, destination, neighbor);
					// set parent of neighbor to current
					neighbor.parent = cur;
					if (!listContains(open, neighbor.x, neighbor.y)) {
						open.add(neighbor);
					}
				}
			}

			try {
				sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void setDestination(PathNode source, PathNode destination) {
		if (source == null) {
			throw new NullPointerException("source cannot be null");
		}
		if (destination == null) {
			throw new NullPointerException("destination cannot be null");
		}
		if (!isRunning()) {
			Game.log("Setting source: " + source + " dest: " + destination);
			// clear out the lists
			open.clear();
			closed.clear();
			// set source and dest
			this.source = source;
			this.destination = destination;
			setName(String.format("(%d, %d) -> (%d, %d)", source.x, source.y, destination.x, destination.y));
			start();
		}
	}

	public void update(Entity entity) {
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			setDestination(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode(0, 0));
		}
	}

}
