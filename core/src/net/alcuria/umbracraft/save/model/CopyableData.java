package net.alcuria.umbracraft.save.model;

/** @author Andrew Keturi
 * @param <T> the model
 * @param <J> */
public interface CopyableData<T, J> {

	public T from(J type);
}
