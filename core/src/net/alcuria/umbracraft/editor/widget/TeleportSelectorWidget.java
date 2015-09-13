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
	private final TeleportDefinition teleport;
	private final ObjectMap<TeleportDirection, SuggestionWidget> textFields = new ObjectMap<>();

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
				teleport.adjacentMaps.put(selectBox.getSelected(), textField.getTextField().getText());
				update();
			}
		};
	}

	/** @param direction the {@link TeleportDefinition} to delete
	 * @return the listener to invoke when a row is deleted. */
	private Listener deleteListener(final TeleportDirection direction) {
		return new Listener() {

			@Override
			public void invoke() {
				textFields.remove(direction);
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
		return Editor.db().maps();
	}

	/** Updates the widget, clearing and rebuilding the rows. */
	public void update() {
		root.clear();
		root.add(new Table() {
			{
				for (final TeleportDirection direction : teleport.adjacentMaps.keys()) {
					add(new Table() {
						{
							// [direction label] [textfield] [close x]
							add(new VisLabel(direction.toString()));
							if (!textFields.containsKey(direction)) {
								final SuggestionWidget textField = new SuggestionWidget(suggestions(), 200);
								textFields.put(direction, textField);
							}
							add(textFields.get(direction).getActor());
							if (teleport.adjacentMaps.get(direction) != null) {
								textFields.get(direction).getTextField().setText(teleport.adjacentMaps.get(direction));
							}
							add(WidgetUtils.button("X", deleteListener(direction)));
						}

					}).row();
				}
				// get all available directions that we haven't already used
				final Array<TeleportDirection> availableDirections = new Array<>();
				for (final TeleportDirection direction : TeleportDirection.values()) {
					if (!teleport.adjacentMaps.containsKey(direction)) {
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

		});

	}
}