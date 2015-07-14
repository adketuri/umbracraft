package net.alcuria.umbracraft.engine.windows;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** A layout for a {@link Window}
 * @author Andrew Keturi */
public abstract class WindowLayout {
	protected Table content;
	protected Table root;
	protected Stage stage;

	/** override this to create the layout */
	public abstract void create();

}
