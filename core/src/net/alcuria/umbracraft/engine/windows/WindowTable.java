package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand.MessageStyle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;

/** A table with the background set to the game's window styling. Use
 * {@link WindowTable#put(Actor)} to add things to the table.
 * @author Andrew Keturi */
public class WindowTable extends Table {

	private Table content;

	public WindowTable() {
		this(MessageStyle.NORMAL);
	}

	public WindowTable(final MessageStyle style) {

		stack(new Table() {
			{
				if (style.bg != null) {
					add(new Table() {
						{
							setBackground(new TiledDrawable(Drawables.skin(style.bg)));
						}
					}).expand().fill().pad(3);
				}
			}
		}, new Table() {
			{
				if (style.frame != null) {
					setBackground(Drawables.ninePatch(style.frame));
				}
				add(content = new Table()).expand().fill();
			}
		}).expand().fill();

	}

	public Cell<?> newLine() {
		return content.row();
	}

	/** Equivalent to add, but puts the content in the correct spot.
	 * @param actor the actor to add to the table.
	 * @return a {@link Cell} */
	public <T extends Actor> Cell<T> put(T actor) {
		return put(actor, false);
	}

	/** Equivalent to add, but puts the content in the correct spot.
	 * @param actor the actor to add to the table.
	 * @return a {@link Cell} */
	public <T extends Actor> Cell<T> put(T actor, boolean expand) {
		return content.add(actor).expand(expand, expand).fill(expand, expand);
	}
}
