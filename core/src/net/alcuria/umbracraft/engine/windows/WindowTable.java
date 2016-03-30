package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.editor.Drawables;

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

		stack(new Table() {
			{
				add(new Table() {
					{
						setBackground(new TiledDrawable(Drawables.skin("ui/bg")));
					}
				}).expand().fill().pad(3);
			}
		}, new Table() {
			{
				setBackground(Drawables.ninePatch("ui/frame"));
				add(content = new Table()).expand().fill();
			}
		});

	}

	/** Equivalent to add, but puts the content in the correct spot.
	 * @param actor the actor to add to the table.
	 * @return a {@link Cell} */
	public <T extends Actor> Cell<T> put(T actor) {
		return content.add(actor);
	}
}
