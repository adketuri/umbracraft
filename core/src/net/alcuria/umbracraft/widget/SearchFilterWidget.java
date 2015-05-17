package net.alcuria.umbracraft.widget;

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
import com.kotcrab.vis.ui.widget.VisTextField.TextFieldListener;

/** A {@link VisTextField} with a selectable list of suggestions that appear
 * underneath. Suggestions are passed into the constructor. To add the widget to
 * the scene simply call {@link SearchFilterWidget#getActor()}
 * @author Andrew Keturi */
public class SearchFilterWidget {

	private static final int MAX_SUGGESTIONS = 20;
	private final Array<String> allSuggestions;
	private final Array<String> curSuggestions;
	private Table suggestionTable;
	private VisTextField textField;

	/** @param suggestions an array of ALL possible suggestions for this widget.
	 *        Up to {@link SearchFilterWidget#MAX_SUGGESTIONS} can be displayed
	 *        at a given time. */
	public SearchFilterWidget(Array<String> suggestions) {
		allSuggestions = suggestions;
		curSuggestions = new Array<String>();

	}

	/** @return the widget */
	public Table getActor() {
		textField = new VisTextField();
		textField.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(VisTextField textField, char c) {
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

		});
		return new Table() {
			{
				add(textField).height(25).expand().fill().row();
				stack(new Table(), suggestionTable = new Table()).expand().fill().height(25);
			}
		};
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
