package net.alcuria.umbracraft.save.model;

import net.alcuria.umbracraft.listeners.Listener.SuccessListener;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Save/Load operations for game data
 * @author Andrew Keturi */
public interface Saveable extends Disposable {

	/** @return all save profiles */
	Array<SaveProfile> getProfiles();

	/** Save the files to disk */
	void saveProfiles(SuccessListener result);

	/** saves a particular file */
	void setProfile(SaveProfile profile, int index);

}
