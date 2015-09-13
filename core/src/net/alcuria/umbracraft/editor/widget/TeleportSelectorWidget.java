package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.editor.Editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;

/** A widget for displaying and adding teleport locations to an
 * {@link AreaNodeDefinition}.
 * @author Andrew Keturi */
public class TeleportSelectorWidget {

	private final Table root = new Table();
	private final ObjectMap<String, SuggestionWidget> suggestionWidgets = new ObjectMap<>();
	private final TeleportDefinition teleport;

	/** Initializes the member variables only. Call
	 * {@link TeleportSelectorWidget#getActor()} to return the actor.
	 * @param teleport the {@link TeleportDefinition} */
	public TeleportSelectorWidget(TeleportDefinition teleport) {
		this.teleport = teleport;
	}

	/** Adds a direction to the list of all available
	 * @param selectBox the dropdown
	 * @param textField the suggestion textField
	 * @return */
	private Listener addDirectionListener(final VisSelectBox<TeleportDirection> selectBox, final SuggestionWidget textField) {
		return new Listener() {

			@Override
			public void invoke() {
				teleport.adjacentMaps.put(selectBox.getSelected().toString(), textField.getTextField().getText());
				update();
			}
		};
	}

	/** @param direction the {@link TeleportDefinition} to delete
	 * @return the listener to invoke when a row is deleted. */
	private Listener deleteListener(final String direction) {
		return new Listener() {

			@Override
			public void invoke() {
				suggestionWidgets.remove(direction);
				teleport.adjacentMaps.remove(direction.toString());
				update();
			}
		};
	}

	/** Creates the widget.
	 * @return a {@link Table} containing the widget. */
	public Table getActor() {
		update();
		return root;
	}

	/** @return All suggestions (map names) */
	private Array<String> suggestions() {
		Array<String> suggestions = new Array<>();
		for (String area : Editor.db().areas().keys()) {
			suggestions.add(area);
		}
		return suggestions;
	}

	/** Updates the widget, clearing and rebuilding the rows. */
	public void update() {
		root.clear();
		root.add(new Table() {
			{
				for (final String direction : teleport.adjacentMaps.keys()) {
					add(new Table() {
						{
							// [direction label] [textfield] [close x]
							add(new VisLabel(direction.toString())).width(80);
							if (!suggestionWidgets.containsKey(direction)) {
								final SuggestionWidget textField = new SuggestionWidget(suggestions(), 200);
								suggestionWidgets.put(direction, textField);
							}
							add(suggestionWidgets.get(direction).getActor());
							if (teleport.adjacentMaps.get(direction.toString()) != null) {
								suggestionWidgets.get(direction).getTextField().setText(teleport.adjacentMaps.get(direction.toString()));
							}
							add(WidgetUtils.button("X", deleteListener(direction)));
						}

					}).row();
				}
				// get all available directions that we haven't already used
				final Array<TeleportDirection> availableDirections = new Array<>();
				for (final TeleportDirection direction : TeleportDirection.values()) {
					if (!teleport.adjacentMaps.containsKey(direction.toString())) {
						availableDirections.add(direction);
					}
				}
				// [direction dropdown] [textfield] [add]
				if (availableDirections.size > 0) {
					add(new Table() {
						{
							final VisSelectBox<TeleportDirection> selectBox = new VisSelectBox<TeleportDirection>();
							selectBox.setItems(availableDirections);
							add(selectBox);
							final SuggestionWidget textField = new SuggestionWidget(suggestions(), 200);
							add(textField.getActor());
							add(WidgetUtils.button("Add", addDirectionListener(selectBox, textField)));
						}
					}).row();
				}
			}

		}).pad(10);

	}
}