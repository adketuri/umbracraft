package net.alcuria.umbracraft.engine.manager.hud;

import net.alcuria.umbracraft.engine.entities.BaseEntity;
import net.alcuria.umbracraft.engine.manager.Manager;

/** Manages an array of hud items and renders/updates them
 * @author Andrew Keturi */
public class HudManager extends Manager<BaseEntity> {

	public HudManager() {
		add(new DebugHudItem());
	}
}
