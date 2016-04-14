package net.alcuria.umbracraft.listeners;

/** A basic listener.
 * @author Andrew Keturi */
public interface Listener {
	public static interface SuccessListener {
		public void fail(String message);

		public void success(String message);
	}

	public void invoke();
}
