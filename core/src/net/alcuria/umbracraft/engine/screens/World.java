package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.manager.hud.HudManager;
import net.alcuria.umbracraft.engine.manager.input.OnscreenInputManager;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.windows.WindowStack;

/** All objects live in the World. Enitities are rendered, the view unprojects,
 * and then ui elements are displayed.
 * @author Andrew Keturi */
public class World implements UmbraScreen, EventListener {
	private HudManager hud;
	private OnscreenInputManager in;
	private Map map;
	private Teleporter teleporter;
	private WindowStack windows;

	@Override
	public void dispose() {
		Game.entities().dispose();
		windows.dispose();
		teleporter.dispose();
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
		Game.entities().render();
		Game.batch().setProjectionMatrix(Game.view().getUiCamera().combined);
		hud.render();
		in.render();
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
		hud = new HudManager();
		in = new OnscreenInputManager();
		windows = new WindowStack();
		teleporter = new Teleporter();
		Game.publisher().subscribe(this);
		Game.entities().create(WorldUtils.getStartingMapName());
		Game.map().create(WorldUtils.getStartingMapName());
		Game.areas().setAreaAndNode(Game.db().config().startingArea, Game.db().config().startingNode);
	}

	@Override
	public void update(float delta) {
		Game.entities().update(delta);
		hud.update();
		in.update();
		Game.view().update();
		windows.update();
		teleporter.update();
	}
}
