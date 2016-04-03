package net.alcuria.umbracraft.sounds;

import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/** Handles playing in-game sound effects and music.
 * @author Andrew Keturi */
public class AudioManager {

	/** Volume of music */
	private float bgmVol = 0.6f;
	/** The last played sound effect */
	private String lastMusic;
	/** Volume of sound effects */
	private float sfxVol = 1;

	/** Plays a sound after some delay.
	 * @param delay the delay, in seconds
	 * @param path the path to the sound */
	public void delayedSound(final float delay, final String path) {
		Timer.schedule(new Task() {

			@Override
			public void run() {
				sound(path);
			}
		}, delay);
	}

	/** Plays background music, stopping any music that might have previously
	 * been playing. */
	public void music(String path) {
		stop();
		lastMusic = path;
		final Music music = Game.assets().get(path, Music.class);
		music.setLooping(true);
		music.setVolume(bgmVol);
		music.play();
	}

	/** Pauses whatever is currently playing. */
	public void pause() {
		if (lastMusic != null) {
			Game.assets().get(lastMusic, Music.class).pause();
		}
	}

	/** Sets the volume of bgm
	 * @param sfxVol the new volume, 0-1 where 0 is muted and 1 is max */
	public void setMusicVolume(float bgmVol) {
		this.bgmVol = MathUtils.clamp(bgmVol, 0, 1);
	}

	/** Sets the volume of sound effects
	 * @param sfxVol the new volume, 0-1 where 0 is muted and 1 is max */
	public void setSoundVolume(float sfxVol) {
		this.sfxVol = MathUtils.clamp(sfxVol, 0, 1);
	}

	/** Plays a sound effect. Does no checking to ensure the sound is loaded.
	 * @param path the path to the asset */
	public void sound(String path) {
		Game.assets().get(path, Sound.class).play(sfxVol);
	}

	/** Stops any currently-playing music */
	public void stop() {
		if (lastMusic != null) {
			Game.assets().get(lastMusic, Music.class).stop();
			lastMusic = null;
		}
	}
}
