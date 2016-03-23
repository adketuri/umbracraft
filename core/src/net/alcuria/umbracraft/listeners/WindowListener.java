package net.alcuria.umbracraft.listeners;

import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;

public interface WindowListener {

	public void onCancel();

	public void onConfirm();

	public void onDirection(Direction direction);

}
