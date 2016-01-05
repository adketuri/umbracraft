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

	public Editor() {
		db = new Db();
		publisher = new EventPublisher();
	}

}
