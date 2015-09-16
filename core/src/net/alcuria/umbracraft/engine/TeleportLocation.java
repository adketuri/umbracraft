package net.alcuria.umbracraft.engine;

import java.io.Serializable;

public class TeleportLocation implements Serializable {

	private final String name;
	private final int x;
	private final int y;

	public TeleportLocation(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
}
