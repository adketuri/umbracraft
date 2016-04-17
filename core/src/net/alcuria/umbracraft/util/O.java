package net.alcuria.umbracraft.util;

/** Object-level utility functions for parameter validation.
 * @author Andrew Keturi */
public final class O {

	/** Ensures the object is not null. For parameter validation.
	 * @param object */
	public static void notNull(Object object) {
		if (object == null) {
			throw new NullPointerException("Parameter cannot be null: " + object);
		}
	}

	/** Ensures the value is positive. For parameter validation.
	 * @param num */
	public static void positive(int num) {
		if (num <= 0) {
			throw new RuntimeException("Number must be positive: " + num);
		}
	}

	private O() {
		throw new InstantiationError("Cannot instantiate class O");
	}

}
