package net.alcuria.umbracraft.variables;

import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.ObjectMap;

/** Manages states for all of the game's variables. Variables are initially
 * created with the {@link Editor} and in scripts their values change at
 * runtime. This class is responsible for tracking all those changes to keep
 * states out of the definitions and simplify some of the save/load logic.
 * @author Andrew Keturi */
public class VariableManager {

	private final ObjectMap<String, Integer> activeVariables = new ObjectMap<String, Integer>();

	public VariableManager() {
		//TODO: load
	}

	/** Saves all active variables to disk and disposes. */
	public void dispose() {
		// TODO: save
		activeVariables.clear();
	}

	/** @param id the name of the variable
	 * @return <code>true</code> if the id exists in the active variables map. */
	public boolean exists(String id) {
		return StringUtils.isNotEmpty(id) && activeVariables.containsKey(id);
	}

	/** Gets the value of a particular variable
	 * @param id the variable's id
	 * @return the {@link Integer} value of the variable */
	public int get(String id) {
		if (!StringUtils.isNotEmpty(id)) {
			throw new IllegalArgumentException("id cannot be null and must be valid");
		}
		// either get the variable or add it to the map and return 0
		if (activeVariables.containsKey(id)) {
			return activeVariables.get(id);
		} else {
			activeVariables.put(id, 0);
			return 0;
		}

	}

	/** Sets the value of the variable, overwriting whatever was previously
	 * stored at the id
	 * @param id the variable's id
	 * @param value the {@link Integer} value of the variable */
	public void set(String id, int value) {
		if (!StringUtils.isNotEmpty(id)) {
			throw new IllegalArgumentException("id cannot be null and must be valid");
		}
		activeVariables.put(id, value);
	}

}
