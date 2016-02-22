package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

/** A {@link VisTextField} with a selectable list of suggestions that appear
 * underneath. Suggestions are passed into the constructor. To add the widget to
 * the scene simply call {@link SuggestionWidget#getActor()}.
 * @author Andrew Keturi */
public class SuggestionWidget {
	private static final int MAX_SUGGESTIONS = 15;
	private final Array<String> allSuggestions;
	private final Array<String> curSuggestions;
	private int highlightIndex = -1;
	private final Array<Label> highlightLabels = new Array<Label>();
	private final Array<Table> highlightTables = new Array<Table>();
	private Listener selectListener;
	private Table suggestionTable;
	private VisTextField textField;
	private boolean useCustomListener = false;
	private final int width;

	/** Creates a widget with a default width of 150 and no custom key typed
	 * listener.
	 * @param suggestions an {@link Array} of suggestions */
	public SuggestionWidget(Array<String> suggestions) {
		this(suggestions, 150);
	}

	/** Creates a widget with a custom width but no custom key typed listener
	 * @param suggestions an {@link Array} of suggestions
	 * @param width the width of the widget in px */
	public SuggestionWidget(Array<String> suggestions, int width) {
		this(suggestions, width, false);
	}

	/** Creates a widget with all custom parameters
	 * @param suggestions an {@link Array} of suggestions
	 * @param width the width of the widget in px
	 * @param useCustomListener whether or not you intend to use a custom
	 *        listener on key typed. <b>IMPORTANT</b> if you set this to
	 *        <code>true</code>, be sure to actually set the listener to invoke
	 *        on key typed otherwise you won't get suggestions.
	 *        {@link SuggestionWidget#setGenericPopulate(Listener)} does what
	 *        you want. */
	public SuggestionWidget(Array<String> suggestions, int width, boolean useCustomListener) {
		allSuggestions = suggestions;
		curSuggestions = new Array<String>();
		this.width = width;
		this.useCustomListener = useCustomListener;
	}

	/** Adds a listener that is invoked when the user selects a suggestion
	 * @param listener the {@link Listener} */
	public void addSelectListener(Listener listener) {
		selectListener = listener;
	}

	/** @return the widget, neatly wrapped into a table. */
	public Table getActor() {
		textField = new VisTextField();
		if (!useCustomListener) {
			setGenericPopulate(null);
		}
		return new Table() {
			{
				add(textField).width(width).height(25).expand().fill().row();
				add(suggestionTable = new Table() {
					@Override
					public void act(float delta) {
						if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
							if (highlightIndex < curSuggestions.size - 1) {
								highlightIndex++;
							}
						}
						if (Gdx.input.isKeyJustPressed(Keys.UP)) {
							if (highlightIndex > 0) {
								highlightIndex--;
							}
						}
						if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
							if (highlightIndex >= 0 && highlightIndex < highlightLabels.size) {
								onSelection(highlightLabels.get(highlightIndex));
							}
						}
						for (int i = 0; i < highlightLabels.size; i++) {
							if (i == highlightIndex) {
								highlightLabels.get(i).setColor(0, 0, 0, 1);
								highlightTables.get(i).setBackground(Drawables.get("yellow"));
							} else {
								highlightLabels.get(i).setColor(1, 1, 1, 1);
								highlightTables.get(i).setBackground(Drawables.get("blue"));
							}
						}
					};
				}).expand().fill();
			}
		};
	}

	/** @return the {@link VisTextField} */
	public VisTextField getTextField() {
		return textField;
	}

	/** Called when a selection is chosen
	 * @param label the {@link Label} */
	private void onSelection(final Label label) {
		textField.setText(label.getText().toString());
		if (selectListener != null) {
			selectListener.invoke();
		}
		suggestionTable.clear();
	}

	/** this MUST be called elsewhere to update suggestions, like when a key is
	 * pressed. If you use {@link SuggestionWidget#setGenericPopulate(Listener)}
	 * this handles that for you. Check your constructor. */
	public void populateSuggestions() {
		curSuggestions.clear();
		if (textField.getText().length() > 0) {
			for (String s : allSuggestions) {
				if (s.toLowerCase().contains(textField.getText().toLowerCase())) {
					curSuggestions.add(s);
					if (curSuggestions.size >= MAX_SUGGESTIONS) {
						break;
					}
				}
			}
		}
		updateSuggestions();
	}

	/** Gives the {@link SuggestionWidget} a generic populate method. Does not
	 * save fields or anything.
	 * @param listener a {@link Listener} to invoke. May be <code>null</code>. */
	public void setGenericPopulate(final Listener listener) {
		textField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(VisTextField textField, char c) {
				populateSuggestions();
				if (listener != null) {
					listener.invoke();
				}
			}
		});
	}

	/** Called when the field input changes to rebuild the suggestions list */
	private void updateSuggestions() {
		suggestionTable.clear();
		highlightLabels.clear();
		highlightTables.clear();
		for (int i = 0; i < curSuggestions.size; i++) {
			final VisLabel label = new VisLabel(curSuggestions.get(i));
			label.setAlignment(Align.left);
			final int idx = i;
			suggestionTable.add(new Table() {
				{
					highlightLabels.add(label);
					highlightTables.add(this);
					WidgetUtils.divider(this, "black");
					add(label).expand().fill().padLeft(5);
					setTouchable(Touchable.enabled);
					setBackground(Drawables.get("blue"));
					addListener(new ClickListener() {

						@Override
						public void clicked(InputEvent event, float x, float y) {
							onSelection(label);
						}

						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							highlightIndex = idx;
						}

					});
				}
			}).expandX().fill().height(30).row();
		}
		highlightIndex = MathUtils.clamp(highlightIndex, 0, highlightLabels.size - 1);
		suggestionTable.add().expand().fill();
	}
}
