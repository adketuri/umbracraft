package net.alcuria.umbracraft.engine.manager;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.entities.BaseEntity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.TintScreenEvent;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

/** Handles screen overlays, which are rendered over all {@link UmbraScreen}
 * screens. Listens for {@link TintScreenEvent} events to adjust screen tint.
 * @author Andrew Keturi */
public class OverlayManager implements EventListener, BaseEntity, Disposable {

	private final Image overlay = new Image(Drawables.skin("ui/black"));
	private final Stage stage = new Stage(Game.view().getViewport());

	{
		overlay.setWidth(Config.viewWidth);
		overlay.setHeight(Config.viewHeight);
		overlay.getColor().a = 0;
		stage.addActor(overlay);
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose() {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof TintScreenEvent) {
			final TintScreenEvent tint = (TintScreenEvent) event;
			overlay.addAction(Actions.sequence(Actions.alpha(tint.target, tint.duration), Actions.run(new Runnable() {
				@Override
				public void run() {
					if (tint.listener != null) {
						tint.listener.invoke();
					}
				}
			})));
		}
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void update() {
		stage.act(Gdx.graphics.getDeltaTime());
	}

}
