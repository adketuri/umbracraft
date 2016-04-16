package net.alcuria.umbracraft.util;

/** Object-level utility function.
 * @author Andrew Keturi */
public class O {

	/** Ensures the object is not null. For parameter validation.
	 * @param object */
	public static void notNull(Object object) {
		if (object == null) {
			throw new NullPointerException("Parameter cannot be null: " + object);
		}
	}

}
