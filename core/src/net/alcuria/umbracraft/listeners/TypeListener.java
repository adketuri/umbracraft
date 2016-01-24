package net.alcuria.umbracraft.listeners;

/** A listener with a single generic argument.
 * @author Andrew Keturi
 * @param <T> some type for the caller */
public interface TypeListener<T> {
	public void invoke(T type);
}
