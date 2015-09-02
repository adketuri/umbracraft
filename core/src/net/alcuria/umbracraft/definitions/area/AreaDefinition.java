package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.Definition;

/** An Area consists of several nodes, which eventually become maps the player
 * can explore. Nodes are either defined as a pre-existing map or randomly
 * generated with developer-defined constraints.
 * @author Andrew Keturi */
public class AreaDefinition extends Definition {

	/** the name of the area */
	public String name;
	/** the root node */
	public AreaNodeDefinition root;

	/** Deletes a node.
	 * @param definition the {@link AreaNodeDefinition} */
	public void deleteNode(AreaNodeDefinition root, AreaNodeDefinition target) {
		Game.log(root.name);
		if (root == target) {
			root = null;
		} else if (root != null && root.children != null) {
			for (AreaNodeDefinition child : root.children) {
				deleteNode(child, target);
			}
		}
	}

	@Override
	public String getName() {
		return name != null ? name : "";
	}
}
