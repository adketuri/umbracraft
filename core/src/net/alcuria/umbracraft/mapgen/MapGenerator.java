package net.alcuria.umbracraft.mapgen;

import java.awt.Point;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/** Generates a map into a 2d array. Also contains some methods to render the map
 * using simple shapes.
 * @author Andrew Keturi */
public class MapGenerator implements InputProcessor {

	Point entrance, exit;
	boolean[][] filled;
	int[][] map;
	int mapH = 3;
	int mapW = 5;
	ShapeRenderer renderer = new ShapeRenderer();
	int sz = 8;

	public MapGenerator() {

		generate();
	}

	/** Clears the filled map */
	private void clearFilled() {
		for (int i = 0; i < filled.length; i++) {
			for (int j = 0; j < filled[0].length; j++) {
				filled[i][j] = false;
			}
		}
	}

	public void draw(SpriteBatch batch) {
		renderer.setAutoShapeType(true);
		renderer.begin();
		renderer.setColor(Color.WHITE);
		for (int i = 0; i < map.length; i++) {
			for (int j = map[0].length - 1; j >= 0; j--) {
				renderer.set(filled[i][j] ? ShapeType.Filled : ShapeType.Line);
				renderer.setColor(filled[i][j] ? Color.WHITE : Color.GRAY);
				renderer.rect(i * sz + 50, j * sz + 50, sz, sz);
			}
		}
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.GREEN);
		renderer.rect(entrance.x * sz + 50, entrance.y * sz + 50, sz, sz);
		renderer.setColor(Color.YELLOW);
		renderer.rect(exit.x * sz + 50, exit.y * sz + 50, sz, sz);
		renderer.end();
	}

	/** Given coordinates, fills a random adjacent point, if it is valid */
	private void fillNearby(int i, int j) {
		i += MathUtils.random(-1, 1);
		j += MathUtils.random(-1, 1);
		if (i >= 0 && i < mapW && j >= 0 && j < mapH) {
			filled[i][j] = true;
		}
	}

	/** Given a point, sets it to filled */
	private void fillPoint(Point p) {
		if (p.x >= 0 && p.y >= 0 && p.x < filled.length && p.y < filled[0].length) {
			filled[p.x][p.y] = true;
		}
	}

	/** Iterates thru the filled tiles and if an unfilled tile is found with 4
	 * adjacent filled tiles, we fill it in. */
	private void fillSuffocated() {
		for (int i = 1; i < filled.length - 1; i++) {
			for (int j = 1; j < filled[0].length - 1; j++) {
				if (!filled[i][j] && filled[i + 1][j] && filled[i - 1][j] && filled[i][j + 1] && filled[i][j - 1]) {
					filled[i][j] = true;
				}
			}
		}

	}

	/** Fills a weighted path from a starting point to an ending point */
	private void fillWalk(Point start, Point end) {
		fillPoint(start);
		if (start.equals(end)) {
			return;
		} else {
			int dX = 0, dY = 0;
			do {
				float xDist = Math.abs(end.x - start.x);
				float yDist = Math.abs(end.y - start.y);
				float xWeight = (xDist / (xDist + yDist));
				if (MathUtils.random() < xWeight) {
					// try to step x
					if (end.x > start.x) {
						dX = MathUtils.random(0, 1);
					} else if (end.x < start.x) {
						dX = MathUtils.random(-1, 0);
					}
				} else {
					// try to step y
					if (end.y > start.y) {
						dY = MathUtils.random(0, 1);
					} else if (end.y < start.y) {
						dY = MathUtils.random(-1, 0);
					}
				}

			} while (dX != 0 && dY != 0);
			Point startInterval = new Point(start);
			startInterval.x += dX;
			startInterval.y += dY;
			fillWalk(startInterval, end);
		}

	}

	/** Given a starting point, some point values, and an offset to that values
	 * array, this finds and returns the nearest point (as a new point) */
	private Point findNearest(Point start, Array<Point> values, int indexOffset) {
		double nearestDist = Double.MAX_VALUE;
		Point nearest = new Point(start);
		for (int i = indexOffset; i < values.size; i++) {
			final Point p = values.get(i);
			if (!p.equals(start) && p.distance(start) < nearestDist) {
				nearestDist = p.distance(start);
				nearest = p;
			}
		}
		return nearest;

	}

	public void generate() {
		long startTime = System.nanoTime();
		int widenTimes = MathUtils.random(2, 10);
		int margin = widenTimes + 1;
		int entranceAttempts = 50;
		int intervalPtSize = MathUtils.random(2, 100);
		mapW = MathUtils.random(40, 85);
		mapH = MathUtils.random(40, 85);
		int minEntranceExitDistance = (mapW + mapH) / 4;
		map = new int[mapW][mapH];
		filled = new boolean[mapW][mapH];
		entrance = new Point();
		exit = new Point();
		// --------
		clearFilled();
		// set the entrance and exit points, assuring they aren't too close
		// together if possible
		do {
			setEdgePoint(entrance);
			setEdgePoint(exit);
			entranceAttempts--;
		} while (entranceAttempts > 0 && entrance.distance(exit) < minEntranceExitDistance);
		fillPoint(entrance);
		fillPoint(exit);
		// create the interval points
		Array<Point> intervals = new Array<>();
		for (int i = 0; i < intervalPtSize; i++) {
			intervals.add(new Point(MathUtils.random(margin, mapW - margin), MathUtils.random(margin, mapH - margin)));
		}
		// fill the paths between interval points
		fillWalk(entrance, findNearest(entrance, intervals, 0));
		for (int i = 0; i < intervals.size - 1; i++) {
			fillWalk(intervals.get(i), findNearest(intervals.get(i), intervals, i + 1));
		}
		fillWalk(exit, findNearest(exit, intervals, 0));
		long endTime = System.nanoTime();
		// widen the path
		for (int amt = 0; amt < widenTimes; amt++) {
			boolean[][] filledRef = new boolean[mapW][mapH];
			for (int i = 0; i < filledRef.length; i++) {
				for (int j = 0; j < filledRef[0].length; j++) {
					filledRef[i][j] = filled[i][j];
				}
			}
			for (int i = 0; i < filledRef.length; i++) {
				for (int j = 0; j < filledRef[0].length; j++) {
					if (hasAdjacentEmptySpace(filledRef, i, j)) {
						fillNearby(i, j);
					}
				}
			}
		}
		// clean up any stray unfilled tiles
		fillSuffocated();
		System.out.println(String.format("Generated! (Took %d ns)", (endTime - startTime)));
	}

	public int getHeight() {
		return mapH;
	}

	public int getWidth() {
		return mapW;
	}

	private boolean hasAdjacentEmptySpace(boolean[][] filledRef, int i, int j) {
		if (!filledRef[i][j]) {
			return false;
		}
		// check if each dir is empty (l, r, d, u)
		if (i > 0 && !filledRef[i - 1][j]) {
			return true;
		} else if (i < mapW - 1 && !filledRef[i + 1][j]) {
			return true;
		} else if (j > 0 && !filledRef[i][j - 1]) {
			return true;
		} else if (j < mapH - 1 && !filledRef[i][j + 1]) {
			return true;
		}
		return false;
	}

	public boolean isFilled(int x, int y) {
		if (filled == null || x < 0 || x >= mapW || y < 0 || y >= mapH) {
			return true;
		}
		return filled[x][y];

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (character == 'g') {
			generate();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private void setEdgePoint(Point p) {
		// top, right, bottom, left
		switch (MathUtils.random(3)) {
		case 0:
			p.setLocation(MathUtils.random(mapW - 1), mapH - 1);
			return;
		case 1:
			p.setLocation(mapW - 1, MathUtils.random(mapH - 1));
			return;
		case 2:
			p.setLocation(MathUtils.random(mapW - 1), 0);
			return;
		case 3:
			p.setLocation(0, MathUtils.random(mapH - 1));
			return;
		}

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		generate();
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}
}
