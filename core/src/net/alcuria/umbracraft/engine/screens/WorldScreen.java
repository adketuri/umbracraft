package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.manager.input.OnscreenInputManager;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.windows.WindowStack;

import com.badlogic.gdx.math.Rectangle;

/** All objects live in the World. Enitities are rendered, the view unprojects,
 * and then ui elements are displayed.
 * @author Andrew Keturi */
public class WorldScreen extends UmbraScreen implements EventListener {
	private final OnscreenInputManager in;
	private Map map;
	private final Teleporter teleporter;
	private final WindowStack windows;

	public WorldScreen() {
		windows = new WindowStack();
		teleporter = new Teleporter();
		Game.publisher().subscribe(this);
		Game.entities().create(EntityScope.MAP, WorldUtils.getStartingMapName());
		Game.entities().create(EntityScope.GLOBAL, null);
		Game.map().create(WorldUtils.getStartingMapName());
		Game.view().setBounds(new Rectangle(0, 0, Game.map().getWidth() * Config.tileWidth, Game.map().getHeight() * Config.tileWidth));
		Game.view().focus();
		Game.areas().setAreaAndNode(Game.db().config().startingArea, Game.db().config().startingNode);
		in = new OnscreenInputManager();
	}

	@Override
	public void dispose() {
		Game.entities().dispose(EntityScope.MAP);
		windows.dispose();
		teleporter.dispose();
		in.dispose();
		Game.publisher().unsubscribe(this);
	}

	@Override
	public WindowStack getWindows() {
		return windows;
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
	public void onRender() {
		Game.entities().render();
		if (Game.isDebug()) {
			Game.entities().renderPaths();
		}
		Game.batch().setProjectionMatrix(Game.view().getUiCamera().combined);
		in.render();
		windows.render();
	}

	@Override
	public void onUpdate(float delta) {
		Game.entities().update(delta);
		in.update();
		Game.view().update();
		windows.update();
		teleporter.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}
}
