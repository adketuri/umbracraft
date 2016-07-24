package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.utils.Array;

/** A representation of the player's current party.
 * @author Andrew Keturi */
public class Party {

	private final Array<PartyMember> members = new Array<PartyMember>();

	/** Adds a {@link PartyMember} to the party
	 * @param member a {@link PartyMember} */
	public void addMember(PartyMember member) {
		members.add(member);
	}

	/** Clears out the party */
	public void clear() {
		members.clear();
	}

	/** @param i an index into the party
	 * @return the given {@link PartyMember} */
	public PartyMember get(int i) {
		if (i < 0 || i >= members.size) {
			throw new ArrayIndexOutOfBoundsException("No party member at index " + i);
		}
		return members.get(i);
	}

	/** @return all party members */
	public Array<PartyMember> getMembers() {
		return members;
	}

	/** Sets the default (starting) party as defined in the DB */
	public void loadDefault() {
		for (String hero : Game.db().config().startingParty) {
			addMember(new PartyMember(hero));
		}

	}

	/** @return the size of the party */
	public int size() {
		return members.size;
	}
}
