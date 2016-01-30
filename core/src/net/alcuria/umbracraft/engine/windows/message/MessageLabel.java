package net.alcuria.umbracraft.engine.windows.message;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/** A special kind of label used in messages. Contains support for various
 * effects used with actions. Effects are applied to the level via actions and
 * are parsed as markup in the label. For instance "I [i]love you!" would
 * display the text "I love you!" and make the word 'love' squish around.
 * @author Andrew Keturi */
public class MessageLabel extends Label {

	/** The various effects we can apply to the {@link Label}. Default is
	 * {@link LabelEffect#NONE}.
	 * @author Andrew Keturi */
	public static enum LabelEffect {
		NONE, SQUISHY;

		public static LabelEffect from(String word) {
			if (word.contains("[i]")) {
				return SQUISHY;
			}
			return NONE;
		}
	}

	/** Removes any special markup from the string
	 * @param word the {@link String} to parse
	 * @return the string without special markup */
	public static String parse(String word) {
		return word.replace("[i]", "");
	}

	private LabelEffect effect = LabelEffect.NONE;

	public MessageLabel(CharSequence text, LabelStyle style) {
		super(text, style);
	}

	public MessageLabel(CharSequence text, LabelStyle style, LabelEffect effect) {
		super(text, style);
		this.effect = effect;
	}

	/** @return the {@link LabelEffect} associated with this label */
	public LabelEffect getEffect() {
		return effect;
	}

}
