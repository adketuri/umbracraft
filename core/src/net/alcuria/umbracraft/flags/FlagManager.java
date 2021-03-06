package net.alcuria.umbracraft.flags;

import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.util.StringUtils;

/** Manages states for all of the game's flags. Flags are initially created with
 * the {@link Editor} and in scripts their values change at runtime. This class
 * is responsible for tracking all those changes to keep states out of the
 * definitions and simplify some of the save/load logic.
 * @author Andrew Keturi */
public class FlagManager {

	private final Set<String> enabledFlags = new HashSet<String>();

	public FlagManager() {
		// TODO load saved flags from disk
	}

	/** Disables a flag. This does not do any checks to ensure the flag exists in
	 * the db. The assumption is that only additions need to be checked and
	 * disabling a flag that is already disabled shouldn't cause any issues.
	 * @param flag the {@link Db} name of the flag we want to disable. */
	public void disable(String flag) {
		if (flag != null) {
			enabledFlags.remove(flag);
		}
	}

	/** Cleans up the FlagManager and frees up any resources. */
	public void dispose() {
		enabledFlags.clear();
		// TODO save flags for later?

	}

	/** Enables a flag by the id associated with it in the {@link Db}. If it is
	 * not found, nothing is enabled or added to the set.
	 * @param flag the {@link Db} name of the flag we want to enable. */
	public void enable(String flag) {
		enabledFlags.add(flag);
	}

	/** @return all enabled flags */
	public Set<String> getAll() {
		return enabledFlags;
	}

	/** @param id the flag ID
	 * @return <code>true</code> if the flag is set */
	public boolean isSet(String id) {
		return StringUtils.isNotEmpty(id) && enabledFlags.contains(id);
	}

	/** Sets a flag, either enabling it or disabling it.
	 * @param id
	 * @param enable */
	public void set(String id, boolean enable) {
		if (enable) {
			enable(id);
		} else {
			disable(id);
		}
	}

	/** Adds several flags
	 * @param flags */
	public void setAll(Set<String> flags) {
		enabledFlags.clear();
		if (flags != null) {
			enabledFlags.addAll(flags);
		}
	}
}
