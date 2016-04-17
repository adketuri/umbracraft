package net.alcuria.umbracraft.save.model;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.inventory.Inventory;
import net.alcuria.umbracraft.party.PartyMember;

import com.badlogic.gdx.utils.Array;

public class SaveProfile {

	/** @return a {@link SaveProfile} from the current game. */
	public static SaveProfile fromGame() {
		SaveProfile profile = new SaveProfile();
		profile.party = Game.party().getMembers();
		profile.inventory = Game.items();
		return profile;
	}

	/** Sets everything up when loading */
	public static void toGame(SaveProfile profile) {
		// reset party
		Game.party().clear();
		for (PartyMember member : profile.party) {
			Game.party().addMember(member);
		}
		// reset inventory
		Game.items().reset(profile.inventory);
		Game.log("Loaded slot 1!");

	}

	/** The inventory, including money */
	public Inventory inventory;
	/** The current party */
	public Array<PartyMember> party;
}
