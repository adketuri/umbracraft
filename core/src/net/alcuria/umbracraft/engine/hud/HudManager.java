package net.alcuria.umbracraft.engine.hud;

import net.alcuria.umbracraft.engine.Manager;
import net.alcuria.umbracraft.engine.entities.BaseEntity;

/** Manages an array of hud items and renders/updates them
 * @author Andrew Keturi */
public class HudManager extends Manager<BaseEntity> {

	public HudManager() {
		add(new DebugHudItem());
	}
}
