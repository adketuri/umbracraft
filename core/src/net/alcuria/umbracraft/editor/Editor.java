package net.alcuria.umbracraft.editor;

import net.alcuria.umbracraft.Db;

/** Contains editor-specific singletons.
 * @author Andrew Keturi */
public final class Editor {

	private static Db db;

	/** @return the {@link Db} */
	public static Db db() {
		return db;
	}

	public Editor() {
		db = new Db();
	}

}
