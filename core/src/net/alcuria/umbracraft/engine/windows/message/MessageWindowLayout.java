package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.windows.WindowLayout;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MessageWindowLayout extends WindowLayout {
	private Image faceImage;
	private VisLabel messageLabel;
	private Table nameTable;
	private Stack windowBackground;

	@Override
	public void hide(final Listener completeListener) {
		messageLabel.addAction(Actions.sequence(Actions.delay(0.2f), Actions.parallel(Actions.moveBy(0, -10, 0.2f, Interpolation.pow2In), Actions.alpha(0, 0.2f))));
		nameTable.addAction(Actions.sequence(Actions.parallel(Actions.alpha(0, 0.2f), Actions.moveBy(-10, 0, 0.2f, Interpolation.pow2Out))));
		faceImage.addAction(Actions.sequence(Actions.parallel(Actions.alpha(0, 0.2f), Actions.moveBy(-10, 0, 0.2f, Interpolation.pow2Out))));
		windowBackground.addAction(Actions.sequence(Actions.delay(0.2f), Actions.parallel(Actions.moveBy(0, -10, 0.2f, Interpolation.pow2In), Actions.alpha(0, 0.2f)), Actions.run(new Runnable() {

			@Override
			public void run() {
				completeListener.invoke();
			}
		})));
	}

	public void setMessage(String message) {
		if (messageLabel != null) {
			messageLabel.setText(message);
		}
	}

	@Override
	public void show() {
		windowBackground = new Stack();
		windowBackground.add(new Table() {
			{
				add(new Table() {
					{
						setBackground(new TiledDrawable(Drawables.skin("ui/bg")));
					}
				}).expand().fill().pad(3);
			}
		});
		windowBackground.add(new Table() {
			{
				setBackground(Drawables.ninePatch("ui/frame"));
			}
		});
		Table windowFrame = new Table();
		windowFrame.add(nameTable = new Table() {
			{
				setBackground(Drawables.ninePatch("ui/namePlate"));
				add(new VisLabel("Amiru", new LabelStyle(Game.assets().get("fonts/message.fnt", BitmapFont.class), Color.WHITE))).pad(0, 65, 18, 20);
			}
		}).height(20).expand().padLeft(40).bottom().left().row();
		windowFrame.add(windowBackground).expand().fillX().bottom().height(80).padLeft(40);
		windowBackground.setColor(1, 1, 1, 0);
		content.stack(windowFrame, textTable()).expand().left().bottom().pad(10).padBottom(0);
		// do actions
		nameTable.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(0.2f), Actions.moveBy(-10, 0), Actions.parallel(Actions.alpha(1, 0.2f), Actions.moveBy(10, 0, 0.2f, Interpolation.pow2Out))));
		faceImage.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(0.2f), Actions.moveBy(-10, 0), Actions.parallel(Actions.alpha(1, 0.2f), Actions.moveBy(10, 0, 0.2f, Interpolation.pow2Out))));
		windowBackground.addAction(Actions.sequence(Actions.alpha(0), Actions.parallel(Actions.moveBy(0, 10, 0.2f, Interpolation.pow2Out), Actions.alpha(1, 0.2f))));
		messageLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(0.3f), Actions.alpha(1, 0.2f)));
	}

	private Table textTable() {
		return new Table() {
			{
				faceImage = new Image(Game.assets().get("ui/face/amiru.png", Texture.class));
				faceImage.setScaling(Scaling.none);
				add(faceImage).bottom();
				messageLabel = new VisLabel("", new LabelStyle(Game.assets().get("fonts/message.fnt", BitmapFont.class), Color.WHITE));
				messageLabel.setWrap(true);
				messageLabel.setAlignment(Align.left);
				add(messageLabel).expand().top().size(330, 70).padBottom(16).padTop(6);
			}

		};
	}

}
