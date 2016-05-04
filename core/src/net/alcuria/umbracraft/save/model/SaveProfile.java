package net.alcuria.umbracraft.save.model;

import java.util.Set;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;
import net.alcuria.umbracraft.engine.inventory.Inventory;
import net.alcuria.umbracraft.party.PartyMember;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SaveProfile {

	/** @return a {@link SaveProfile} from the current game. */
	public static SaveProfile fromGame() {
		SaveProfile profile = new SaveProfile();
		profile.stats = Game.stats();
		profile.party = Game.party().getMembers();
		profile.inventory = Game.items();
		profile.location = new Location(Game.areas().getArea(), Game.areas().getNode(), Game.map().getName(), Game.entities().find(Entity.PLAYER).position);
		profile.flags = Game.flags().getAll();
		profile.variables = Game.variables().getAll();
		return profile;
	}

	/** Sets everything up when loading */
	public static void toGame(SaveProfile profile) {
		// set stats
		Game.stats().copy(profile.stats);
		// reset party
		Game.party().clear();
		for (PartyMember member : profile.party) {
			Game.party().addMember(member);
		}
		// reset flags/vars
		Game.variables().setAll(profile.variables);
		Game.flags().setAll(profile.flags);
		// reset inventory
		Game.items().reset(profile.inventory);
		// set location
		Game.areas().setAreaAndNode(profile.location.area, profile.location.node);
		Game.map().create(profile.location.area);
		Game.entities().dispose(EntityScope.MAP);
		Game.entities().create(EntityScope.MAP, profile.location.map);
		Game.entities().find(Entity.PLAYER).position.set(profile.location.position);
		Game.view().clearBounds();
		Game.view().setBounds(new Rectangle(0, 0, Game.map().getWidth() * Config.tileWidth, Game.map().getHeight() * Config.tileWidth));
		Game.view().setTarget(Game.entities().find(Entity.PLAYER));
		Game.view().focus();
		Game.log("Loaded slot 1!");

	}

	/** All flags enabled */
	public Set<String> flags;
	/** The inventory, including money */
	public Inventory inventory;
	/** The location of the character */
	public Location location;
	/** The current party */
	public Array<PartyMember> party;
	/** Game Stats, like time played, etc */
	public GameStatsManager stats;
	/** All variables used */
	public ObjectMap<String, Integer> variables;

}
