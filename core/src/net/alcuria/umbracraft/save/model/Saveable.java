package net.alcuria.umbracraft.save.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Save/Load operations for game data
 * @author Andrew Keturi */
public interface Saveable extends Disposable {

	/** @return all save profiles */
	Array<SaveProfile> getProfiles();

	/** saves a particular file */
	void setProfile(SaveProfile profile, int index);

}
