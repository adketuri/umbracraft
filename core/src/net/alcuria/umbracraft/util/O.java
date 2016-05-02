package net.alcuria.umbracraft.util;

import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;

/** Object-level utility functions for parameter validation.
 * @author Andrew Keturi */
public final class O {

	/** Helpers to invoke listeners while adding null checks
	 * @author Andrew Keturi */
	public static final class L {
		public static void $(Listener listener) {
			if (listener != null) {
				listener.invoke();
			}
		}

		public static <T> void $(TypeListener<T> listener, T arg) {
			if (listener != null) {
				listener.invoke(arg);
			}
		}
	}

	/** Ensures the object is not null. For parameter validation.
	 * @param <T>
	 * @param object */
	public static <T> T notNull(T object) {
		if (object == null) {
			throw new NullPointerException("Parameter cannot be null: " + object);
		}
		return object;
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
