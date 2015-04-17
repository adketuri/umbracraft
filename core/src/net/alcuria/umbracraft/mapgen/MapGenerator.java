package net.alcuria.umbracraft.mapgen;

import java.awt.Point;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class MapGenerator implements InputProcessor {

	boolean[][] filled;
	int[][] map;
	int mapH = 3;
	int mapW = 5;
	Point p1, p2, entrance, exit;
	ShapeRenderer renderer = new ShapeRenderer();
	int sz = 16;

	public MapGenerator() {

		generate();
	}

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
				renderer.rect(i * sz + 50, j * sz + 50, sz, sz);
			}
		}
		renderer.flush();
		renderer.set(ShapeType.Line);
		renderer.setColor(Color.RED);
		renderer.rect(entrance.x * sz + 50, entrance.y * sz + 50, sz, sz);
		renderer.rect(exit.x * sz + 50, exit.y * sz + 50, sz, sz);
		renderer.end();
	}

	private void fillPoint(Point p) {
		if (p.x >= 0 && p.y >= 0 && p.x < filled.length && p.y < filled[0].length) {
			filled[p.x][p.y] = true;
		}
	}

	private void generate() {
		mapW = MathUtils.random(2, 20);
		mapH = MathUtils.random(2, 20);
		map = new int[mapW][mapH];
		filled = new boolean[mapW][mapH];
		p1 = new Point();
		p2 = new Point();
		entrance = new Point();
		exit = new Point();
		// --------
		clearFilled();
		setEdgePoint(p1, entrance);
		setEdgePoint(p2, exit);
		fillPoint(p1);
		fillPoint(p2);
		Point pi = new Point(MathUtils.random(mapW - 1), MathUtils.random(mapH - 1));
		fillPoint(pi);
		System.out.println("Generate");
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

	private void setEdgePoint(Point p, Point edge) {
		// top, right, bottom, left
		switch (MathUtils.random(3)) {
		case 0:
			p.setLocation(MathUtils.random(mapW - 1), mapH - 1);
			edge.setLocation(p.x, p.y + 1);
			return;
		case 1:
			p.setLocation(mapW - 1, MathUtils.random(mapH - 1));
			edge.setLocation(p.x + 1, p.y);
			return;
		case 2:
			p.setLocation(MathUtils.random(mapW - 1), 0);
			edge.setLocation(p.x, p.y - 1);
			return;
		case 3:
			p.setLocation(0, MathUtils.random(mapH - 1));
			edge.setLocation(p.x - 1, p.y);
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
