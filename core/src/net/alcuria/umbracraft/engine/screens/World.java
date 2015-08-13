package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.EntityManager;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.hud.HudManager;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.windows.WindowStack;

/** All objects live in the World. Enitities are rendered, the view unprojects,
 * and then ui elements are displayed.
 * @author Andrew Keturi */
public class World implements UmbraScreen, EventListener {
	private EntityManager entities;
	private HudManager manager;
	private Map map;
	private WindowStack windows;

	@Override
	public void dispose() {
		entities.dispose();
		windows.dispose();
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void hide() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof MapChangedEvent) {
			map.create(((MapChangedEvent) event).id);
		}

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		entities.render();
		Game.batch().setProjectionMatrix(Game.view().getUiCamera().combined);
		manager.render();
		windows.render();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		map = new Map("Andrew");
		entities = Game.entities();
		entities.update(map);
		manager = new HudManager();
		windows = new WindowStack();
		Game.publisher().subscribe(this);
	}

	@Override
	public void update(float delta) {
		entities.update(delta);
		manager.update();
		Game.view().update();
		windows.update();
	}
}
