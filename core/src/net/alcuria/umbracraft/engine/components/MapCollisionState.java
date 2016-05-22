package net.alcuria.umbracraft.engine.components;

/** Enumerates all possible states of the {@link MapCollisionComponent} entity.
 * @author Andrew Keturi */
public enum MapCollisionState {

	FALLING_DOWN, JUMPING_UP, ON_GROUND, ON_PLATFORM, ON_STAIRS;

	/** @return <code>true</code> if we're in a state where we're grounded. In
	 *         other words, any state where we don't want a change in y
	 *         velocity. */
	public boolean isOnGround() {
		return this == ON_GROUND || this == ON_PLATFORM || this == ON_STAIRS;
	}
}
