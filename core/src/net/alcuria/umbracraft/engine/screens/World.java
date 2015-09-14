package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
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
	private HudManager manager;
	private Map map;
	private WindowStack windows;

	@Override
	public void dispose() {
		Game.entities().dispose();
		windows.dispose();
		Game.publisher().unsubscribe(this);
	}

	/** Reads in the starting area/node from the db and fetches the
	 * {@link MapDefinition} name to use.
	 * @return the starting map name {@link String} */
	private String getStartingMapName() {
		final String startingArea = Game.db().config().startingArea;
		final String startingNode = Game.db().config().startingNode;
		final AreaDefinition areaDefinition = Game.db().area(startingArea);
		if (areaDefinition != null) {
			final AreaNodeDefinition areaNodeDefinition = areaDefinition.find(startingNode);
			if (areaNodeDefinition != null) {
				return areaNodeDefinition.mapDefinition;
			}
		}
		Game.error("Starting map was not found");
		return null;
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
		manager = new HudManager();
		windows = new WindowStack();
		Game.publisher().subscribe(this);
		Game.entities().create(getStartingMapName());
		Game.map().create(getStartingMapName());
	}

	@Override
	public void update(float delta) {
		Game.entities().update(delta);
		manager.update();
		Game.view().update();
		windows.update();
	}
}
