package net.alcuria.umbracraft.sounds;

import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/** Handles playing in-game sound effects and music.
 * @author Andrew Keturi */
public class AudioManager {

	/** Volume of music */
	private float bgmVol = 1;
	/** Volume of sound effects */
	private float sfxVol = 1;

	/** Plays background music */
	public void music(String path) {
		Game.assets().get(path, Music.class).play();
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
}
