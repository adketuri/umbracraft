package net.alcuria.umbracraft.engine.manager;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.entities.BaseEntity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.TintScreenEvent;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.O.L;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

/** Handles screen overlays, which are rendered over all {@link UmbraScreen}
 * screens. Listens for {@link TintScreenEvent} events to adjust screen tint.
 * @author Andrew Keturi */
public class OverlayManager implements EventListener, BaseEntity, Disposable {

	private final ColorAction colorAction = new ColorAction();
	private final Image overlay = new Image(Drawables.skin("ui/black"));
	private final Stage stage = new Stage(Game.view().getViewport());
	private boolean startedColorAction = false;
	private Listener tintListener;
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
			if (tint.color == null) {
				overlay.addAction(Actions.sequence(Actions.alpha(tint.target, tint.duration), Actions.run(new Runnable() {
					@Override
					public void run() {
						if (tint.listener != null) {
							tint.listener.invoke();
						}
					}
				})));
			} else {
				colorAction.setColor(Game.batch().getColor());
				colorAction.setEndColor(tint.color);
				colorAction.setDuration(tint.duration);
				colorAction.restart();
				tintListener = tint.listener;
				// if the action has nearly no duration, let's immediately set the color
				if (MathUtils.isEqual(tint.duration, 0)) {
					colorAction.setColor(tint.color);
				}
				startedColorAction = true;
			}
		}
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void update() {
		if (colorAction != null && colorAction.getColor() != null) {
			//Game.log(colorAction.getColor().toString());
		}
		stage.act(Gdx.graphics.getDeltaTime());
		if (startedColorAction) {
			Game.batch().setColor(colorAction.getColor());
			if (colorAction.act(Gdx.graphics.getDeltaTime())) {
				Game.log("Tint complete");
				L.$(tintListener);
				startedColorAction = false;
			}
		}
	}
}
