package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.manager.input.DebugText;
import net.alcuria.umbracraft.engine.manager.input.OnscreenInputManager;
import net.alcuria.umbracraft.engine.windows.WindowStack;
import net.alcuria.umbracraft.party.PartyMember;
import net.alcuria.umbracraft.save.model.GameStatsManager.GameStat;

import com.badlogic.gdx.Gdx;

/** All objects live in the World. Enitities are rendered, the view unprojects,
 * and then ui elements are displayed.
 * @author Andrew Keturi */
public class WorldScreen extends UmbraScreen implements EventListener {
	private final OnscreenInputManager in;
	private final Teleporter teleporter;
	private float time;
	private final WindowStack windows;

	public WorldScreen() {
		windows = new WindowStack();
		teleporter = new Teleporter();
		Game.publisher().subscribe(this);

		// set the starting party
		for (String hero : Game.db().config().startingParty) {
			Game.party().addMember(new PartyMember(hero));
		}

		// set the starting area/map location
		Game.entities().create(EntityScope.MAP, WorldUtils.getStartingMapName());
		Game.entities().create(EntityScope.AREA, Game.db().config().startingArea);

		// create global entities
		Game.entities().create(EntityScope.GLOBAL, null);
		Game.map().create(WorldUtils.getStartingMapName());
		Game.view().setBounds(Game.map().getBounds());
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
			MapChangedEvent evt = (MapChangedEvent) event;
			Game.areas().setAreaAndNode(evt.area, evt.node);
			AreaDefinition area = Game.db().area(evt.area);
			AreaNodeDefinition node = area.find(area.root, evt.node);
			if (node == null) {
				throw new NullPointerException("Node not found in area " + area);
			}
			Game.map().create(node.mapDefinition);
			Game.view().setBounds(Game.map().getBounds());
			Game.view().setTarget(Game.entities().find(Entity.PLAYER));
			Game.view().focus();
			Game.entities().dispose(EntityScope.MAP);
			Game.entities().create(EntityScope.MAP, node.mapDefinition);
			Game.entities().find(Entity.PLAYER).position.set(evt.x, evt.y, 0);
		}
	}

	@Override
	public void onRender() {
		long lastTime = System.nanoTime();
		Game.entities().render();
		DebugText.renderTime = System.nanoTime() - lastTime; //sry
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
		time += Gdx.graphics.getDeltaTime();
		if (time > 1) {
			time -= 1;
			Game.stats().increment(GameStat.TIME_PLAYED, 1);
		}
	}

	@Override
	public void pause() {
		Game.audio().pause();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
		Game.audio().playOverworld();

	}
}
