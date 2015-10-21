package net.alcuria.umbracraft.party;

import com.badlogic.gdx.utils.Array;

/** A representation of the player's current party.
 * @author Andrew Keturi */
public class Party {

	public Array<PartyMember> members;

	public void addMember(PartyMember member) {
		if (members == null) {
			members = new Array<PartyMember>();
		}
		members.add(member);
	}
}
