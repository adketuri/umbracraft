package net.alcuria.umbracraft.editor.events;

import net.alcuria.umbracraft.editor.layout.EditorLayout;
import net.alcuria.umbracraft.engine.events.Event;

import com.badlogic.gdx.math.Vector2;

/** An event to notify the {@link EditorLayout} that we want to show a tooltip.
 * @author Andrew Keturi */
public class ShowTooltip extends Event {

	public String text;
	public float x, y;

	public ShowTooltip(float x, float y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}

	public ShowTooltip(Vector2 coords, String text) {
		this(coords.x, coords.y, text);
	}

}
