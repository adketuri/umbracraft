package net.alcuria.umbracraft.save.model;

import com.badlogic.gdx.utils.ObjectMap;

/** Various game stats we want to save
 * @author Andrew Keturi */
public class GameStatsManager {

	/** A type of stat to track
	 * @author Andrew Keturi */
	public enum GameStat {
		TIME_PLAYED
	}

	private ObjectMap<String, Long> map = new ObjectMap<String, Long>();

	/** Updates this {@link GameStatsManager} object with the fields present in
	 * the passed in stats objet.
	 * @param stats the {@link GameStatsManager} we want to copy */
	public void copy(GameStatsManager stats) {
		if (stats != null && stats.map != null) {
			map = stats.map;
		}
	}

	/** Gets a stat from the manager. Zero is returned if the stat is not
	 * present.
	 * @param stat the {@link GameStat}
	 * @return the value of this stat */
	public long get(GameStat stat) {
		if (!map.containsKey(stat.toString())) {
			return 0L;
		}
		return map.get(stat.toString());
	}

	/** Increments a stat value
	 * @param stat the {@link GameStat}
	 * @param value the value to increment by */
	public void increment(GameStat stat, long value) {
		if (!map.containsKey(stat.toString())) {
			set(stat, value);
		} else {
			map.put(stat.toString(), map.get(stat.toString()) + value);
		}
	}

	/** Sets a stat to a value
	 * @param stat the {@link GameStat}
	 * @param value the new value for this stat */
	public void set(GameStat stat, long value) {
		map.put(stat.toString(), value);
	}
}