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
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MessageWindowLayout extends WindowLayout {

	private static enum MessageState {
		STEP_1_CREATE, STEP_2_ACCEPT_INPUT, STEP_3_MESSAGE_DISPLAYED
	}

	private static final int MESSAGE_WIDTH = 310;
	private static final float SHOW_TRANSITION_TIME = 0.15f;
	private Image faceImage;
	private String message;
	private final Array<VisLabel> messageLabels = new Array<VisLabel>();
	private Table nameTable, messageTable;
	private MessageState state = MessageState.STEP_1_CREATE;
	private final LabelStyle style = new LabelStyle(Game.assets().get("fonts/message.fnt", BitmapFont.class), Color.WHITE);
	private Stack windowBackground;

	/** Advances the message state
	 * @return <code>true</code> when we are ready to close the message */
	public boolean advance() {
		switch (state) {
		case STEP_1_CREATE:
			return false;
		case STEP_2_ACCEPT_INPUT:
			immediatelyShowAllText();
			return false;
		case STEP_3_MESSAGE_DISPLAYED:
			return true;
		}
		return false;
	}

	@Override
	public void hide(final Listener completeListener) {

		for (VisLabel messageLabel : messageLabels) {
			messageLabel.addAction(Actions.sequence(Actions.delay(SHOW_TRANSITION_TIME), Actions.parallel(Actions.moveBy(0, -10, SHOW_TRANSITION_TIME, Interpolation.pow2In), Actions.alpha(0, SHOW_TRANSITION_TIME))));
		}
		nameTable.addAction(Actions.sequence(Actions.parallel(Actions.alpha(0, SHOW_TRANSITION_TIME), Actions.moveBy(-10, 0, SHOW_TRANSITION_TIME, Interpolation.pow2Out))));
		faceImage.addAction(Actions.sequence(Actions.parallel(Actions.alpha(0, SHOW_TRANSITION_TIME), Actions.moveBy(-10, 0, SHOW_TRANSITION_TIME, Interpolation.pow2Out))));
		windowBackground.addAction(Actions.sequence(Actions.delay(SHOW_TRANSITION_TIME), Actions.parallel(Actions.moveBy(0, -10, SHOW_TRANSITION_TIME, Interpolation.pow2In), Actions.alpha(0, SHOW_TRANSITION_TIME)), Actions.run(new Runnable() {

			@Override
			public void run() {
				completeListener.invoke();
			}
		})));
	}

	private void immediatelyShowAllText() {
		setMessage(message, true);
		state = MessageState.STEP_3_MESSAGE_DISPLAYED;
	}

	public void setMessage(String message, boolean instant) {
		this.message = message;
		if (messageTable != null) {
			messageLabels.clear();
			messageTable.clear();

			// split the message by space characters.
			for (String word : message.split(" ")) {
				messageLabels.add(new VisLabel(word, style));
			}

			// go thru every word
			int idx = 0;
			while (idx < messageLabels.size) {
				Table row = new Table();
				int rowWidth = 0;
				// add words to a row till we run out or go too wide
				while (rowWidth < MESSAGE_WIDTH && idx < messageLabels.size) {
					final float labelWidth = messageLabels.get(idx).getPrefWidth();
					row.add(messageLabels.get(idx)).width(labelWidth).padRight(5);
					rowWidth += (labelWidth + 5);
					idx++;
				}
				row.add().expandX().fillX();
				messageTable.add(row).expandX().left().row();
			}
			messageTable.add().expand().fill(); // top aligns everything

			float charPause = instant ? 0f : 0.02f;
			int currentLetters = 0;
			for (final VisLabel word : messageLabels) {
				final String wordText = word.getText().toString();
				final int wordLen = word.getText().length;
				if (!instant) {
					word.setText("");
				}
				word.clearActions();
				word.addAction(Actions.sequence(Actions.delay(SHOW_TRANSITION_TIME + currentLetters * charPause), new TemporalAction(wordLen * charPause) {

					float cur = 0;
					int letters = 0;

					@Override
					protected void end() {
						super.end();
						word.setText(wordText);
					}

					@Override
					protected void update(float percent) {
						cur += percent;
						if (cur > (float) letters / wordLen) {
							letters++;
							if (letters > wordLen) {
								return;
							}
							word.setText(wordText.substring(0, letters));
						}
					}
				}));
				currentLetters += wordLen - 1;
			}
			windowBackground.addAction(Actions.sequence(Actions.delay(SHOW_TRANSITION_TIME + currentLetters * charPause), Actions.run(new Runnable() {

				@Override
				public void run() {
					Game.log("Done: ");
					state = MessageState.STEP_3_MESSAGE_DISPLAYED;
				}
			})));
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
		nameTable.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(SHOW_TRANSITION_TIME), Actions.moveBy(-10, 0), Actions.parallel(Actions.alpha(1, SHOW_TRANSITION_TIME), Actions.moveBy(10, 0, SHOW_TRANSITION_TIME, Interpolation.pow2Out))));
		faceImage.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(SHOW_TRANSITION_TIME), Actions.moveBy(-10, 0), Actions.parallel(Actions.alpha(1, SHOW_TRANSITION_TIME), Actions.moveBy(10, 0, SHOW_TRANSITION_TIME, Interpolation.pow2Out))));
		windowBackground.addAction(Actions.sequence(Actions.alpha(0), Actions.parallel(Actions.moveBy(0, 10, SHOW_TRANSITION_TIME, Interpolation.pow2Out), Actions.alpha(1, SHOW_TRANSITION_TIME)), Actions.run(new Runnable() {

			@Override
			public void run() {
				Game.log("Accepting");
				state = MessageState.STEP_2_ACCEPT_INPUT;
			}
		})));
		messageTable.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(0.3f), Actions.alpha(1, SHOW_TRANSITION_TIME)));
	}

	private Table textTable() {
		return new Table() {
			{
				faceImage = new Image(Game.assets().get("ui/face/amiru.png", Texture.class));
				faceImage.setScaling(Scaling.none);
				add(faceImage).bottom();
				add(messageTable = new Table()).expand().top().size(MESSAGE_WIDTH + 20, 70).padTop(30);
			}
		};
	}
}
