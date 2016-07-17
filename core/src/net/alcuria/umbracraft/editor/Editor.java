package net.alcuria.umbracraft.editor;

import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.engine.events.EventPublisher;

/** Contains editor-specific singletons.
 * @author Andrew Keturi */
public final class Editor {

	private static Db db;
	private static EventPublisher publisher;

	/** @return the {@link Db} */
	public static Db db() {
		return db;
	}

	/** @return the {@link EventPublisher} */
	public static EventPublisher publisher() {
		return publisher;
	}

	/** Reloads the db, allowing the editor's autocomplete to update after
	 * saving. */
	public static void reloadDb() {
		db = new Db();
	}

	public Editor() {
		db = new Db();
		publisher = new EventPublisher();
	}

}
