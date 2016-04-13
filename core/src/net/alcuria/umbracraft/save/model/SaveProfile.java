package net.alcuria.umbracraft.save.model;

import java.io.Serializable;

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

	/** The current party */
	public Array<PartyMember> party;
}
