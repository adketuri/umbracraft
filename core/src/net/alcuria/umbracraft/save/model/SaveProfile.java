package net.alcuria.umbracraft.save.model;

import java.io.Serializable;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.party.PartyMember;

import com.badlogic.gdx.utils.Array;

public class SaveProfile implements Serializable {
	public static class SavePartyMember implements CopyableData<SavePartyMember, PartyMember> {
		private static final long serialVersionUID = 1L;

		@Override
		public SavePartyMember from(PartyMember type) {
			SavePartyMember member = new SavePartyMember();
			return member;
		}

	}

	private static final long serialVersionUID = 1L;

	/** @return a {@link SaveProfile} from the current game. */
	public static SaveProfile fromGame() {
		SaveProfile profile = new SaveProfile();
		profile.party = Game.party().getMembers();
		return profile;
	}

	/** The current party */
	public Array<PartyMember> party;
}
