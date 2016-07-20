package net.alcuria.umbracraft.editor;

import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition;
import net.alcuria.umbracraft.engine.events.EventPublisher;

import com.badlogic.gdx.utils.Array;

/** Contains editor-specific singletons.
 * @author Andrew Keturi */
public final class Editor {

	/** A clipboard for various modules that may need copy/paste functionality.
	 * @author Andrew Keturi */
	public static final class Clipboard {
		public Array<SkillActionDefinition> skillActions;
	}

	private static Clipboard clipboard;
	private static Db db;
	private static EventPublisher publisher;

	public static Clipboard clipboard() {
		return clipboard;
	}

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
		clipboard = new Clipboard();
		db = new Db();
		publisher = new EventPublisher();
	}

}
