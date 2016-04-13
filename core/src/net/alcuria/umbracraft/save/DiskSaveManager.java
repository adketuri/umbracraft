package net.alcuria.umbracraft.save;

import net.alcuria.umbracraft.save.model.SaveProfile;
import net.alcuria.umbracraft.save.model.Saveable;

import com.badlogic.gdx.utils.Array;

/** Manages save data from disk
 * @author Andrew Keturi */
public class DiskSaveManager implements Saveable {

	private Array<SaveProfile> profiles;

	@Override
	public void dispose() {

	}

	@Override
	public Array<SaveProfile> getProfiles() {
		return profiles;
	}

	@Override
	public void setProfile(SaveProfile profile, int index) {

	}

}
