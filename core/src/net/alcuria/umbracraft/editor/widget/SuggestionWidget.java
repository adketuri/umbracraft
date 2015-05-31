package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;

/** A {@link VisTextField} with a selectable list of suggestions that appear
 * underneath. Suggestions are passed into the constructor. To add the widget to
 * the scene simply call {@link SuggestionWidget#getActor()}
 * @author Andrew Keturi */
public class SuggestionWidget {
	private static final int MAX_SUGGESTIONS = 20;
	private final Array<String> allSuggestions;
	private final Array<String> curSuggestions;
	private Listener selectListener;
	private Table suggestionTable;
	private VisTextField textField;
	private final int width;

	/** @param suggestions an array of ALL possible suggestions for this widget.
	 *        Up to {@link SuggestionWidget#MAX_SUGGESTIONS} can be displayed at
	 *        a given time. */
	public SuggestionWidget(Array<String> suggestions, int width) {
		allSuggestions = suggestions;
		curSuggestions = new Array<String>();
		this.width = width;
	}

	/** Adds a listener that is invoked when the user selects a suggestion
	 * @param listener the {@link Listener} */
	public void addSelectListener(Listener listener) {
		selectListener = listener;
	}

	/** @return the widget */
	public Table getActor() {
		textField = new VisTextField();
		return new Table() {
			{
				add(textField).width(width).height(25).expand().fill().row();
				//stack(new Table(), suggestionTable = new Table()).expand().fill().height(25);
				add(suggestionTable = new Table()).expand().fill();

			}
		};
	}

	/** @return the {@link VisTextField} */
	public VisTextField getTextField() {
		return textField;
	}

	/** this MUST be called elsewhere to update suggestions, like when a key is
	 * pressed */
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

	/** Called when the field input changes to rebuild the suggestions list */
	private void updateSuggestions() {
		suggestionTable.clear();
		for (String str : curSuggestions) {
			final VisLabel label = new VisLabel(str);
			label.setAlignment(Align.left);
			suggestionTable.add(new Table() {
				{
					WidgetUtils.divider(this, "black");
					add(label).expand().fill().padLeft(5);
					setTouchable(Touchable.enabled);
					setBackground(Drawables.get("blue"));
					addListener(new ClickListener() {

						@Override
						public void clicked(InputEvent event, float x, float y) {
							textField.setText(label.getText().toString());
							if (selectListener != null) {
								selectListener.invoked();
							}
							suggestionTable.clear();
						}

						@Override
						public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
							label.setColor(0, 0, 0, 1);
							setBackground(Drawables.get("yellow"));
						}

						@Override
						public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
							label.setColor(1, 1, 1, 1);
							setBackground(Drawables.get("blue"));

						}
					});
				}
			}).expandX().fill().height(30).row();
		}
		suggestionTable.add().expand().fill();
	}
}
