package net.alcuria.umbracraft.engine.audio;

import net.alcuria.umbracraft.sounds.AudioManager;

/** Interfaces with the implementing class to play sounds, etc. This is weird and
 * I should probably be doing it some other way.
 * @author Andrew Keturi */
public abstract class Audio {

	public static enum CommonSound {
		CLOSE
	}

	public AudioManager manager = new AudioManager();

	/** Pause the music */
	public void pause() {
		manager.pause();
	}

	/** Play the battle theme */
	public abstract void playBattle();

	/** Play the overworld music */
	public abstract void playOverworld();

	/** Play the victory theme */
	public abstract void playVictory();

	/** Play some common sound effect for UI or something */
	public abstract void sound(CommonSound sound);

}
