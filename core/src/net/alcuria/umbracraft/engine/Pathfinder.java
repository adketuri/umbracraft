package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/** Handles pathfinding through a {@link DirectedInputComponent}
 * @author Andrew Keturi */
public class Pathfinder {

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
			//			Game.debug(color + " " + x + " " + y);
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
	private final Array<PathNode> solution = new Array<PathNode>();

	public Pathfinder(DirectedInputComponent component) {
		this.component = component;
	}

	public Array<PathNode> getSolution() {
		return solution;
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
		if (solution != null) {
			for (PathNode n : solution) {
				n.draw(Color.CYAN, Game.map().getAltitudeAt(n.x, n.y), Game.batch());
			}
		}
	}

	/** Sets a destination to attempt to find a path to
	 * @param source the starting {@link PathNode}
	 * @param destination the ending {@link PathNode} */
	public void setTarget(PathNode source, PathNode destination) {
		if (source == null) {
			throw new NullPointerException("source cannot be null");
		}
		if (destination == null) {
			throw new NullPointerException("destination cannot be null");
		}
		// clear out the lists
		open.clear();
		closed.clear();
		// set source and dest
		this.source = source;
		this.destination = destination;
		this.source.f = Heuristic.calculateFCost(source, destination, source);
		new Thread("Pathfinder") {
			@Override
			public void run() {
				solve();
			};
		}.start();
	}

	private void solve() {
		final Map map = Game.map();
		final int[] dX = { 0, 1, 1, 1, 0, -1, -1, -1 }; // clockwise, from 12 oclock
		final int[] dY = { 1, 1, 0, -1, -1, -1, 0, 1 };
		open.add(source);
		while (true) {

			// ensure we still have open nodes
			if (open.size <= 0) {
				Game.debug("No path found");
				break;
			}

			// get a pointer to the node in the open list with the lowest f cost
			open.sort();
			PathNode cur = open.removeIndex(0);
			closed.add(cur);

			if (cur.hasSameLocationAs(destination)) {
				Game.debug("Path found!");
				solution.clear();
				while (cur != null) {
					solution.add(cur);
					cur = cur.parent;
				}
				break;
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

		}
	}

	public void update(Entity entity) {
		//		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
		//			Entity hero = Game.entities().find(Entity.PLAYER);
		//			setTarget(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode((int) (hero.position.x / Config.tileWidth), (int) (hero.position.y / Config.tileWidth)));
		//		}
	}

}
