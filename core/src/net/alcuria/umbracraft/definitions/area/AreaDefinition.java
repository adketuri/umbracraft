package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** An Area consists of several nodes, which eventually become maps the player
 * can explore. Nodes are either defined as a pre-existing map or randomly
 * generated with developer-defined constraints.
 * @author Andrew Keturi */
public class AreaDefinition extends Definition {

	/** the name of the area */
	public String name;
	/** the root node */
	public AreaNodeDefinition root;

	/** Recursively adds a node and its children
	 * @param nodes
	 * @param root */
	private void addNodes(Array<AreaNodeDefinition> nodes, AreaNodeDefinition root) {
		nodes.add(root);
		if (root.children != null) {
			for (AreaNodeDefinition child : root.children) {
				addNodes(nodes, child);
			}
		}
	}

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

	/** Finds a node by name, recursively searching the node's children.
	 * @param rootNode the starting node
	 * @param target the name we're looking for
	 * @return */
	public AreaNodeDefinition find(AreaNodeDefinition rootNode, String target) {
		if (rootNode != null && rootNode.name != null) {
			if (rootNode.name.equals(target)) {
				return rootNode;
			} else if (rootNode.children != null) {
				for (AreaNodeDefinition childNode : rootNode.children) {
					AreaNodeDefinition child = find(childNode, target);
					if (child != null) {
						return child;
					}
				}
			}
		}
		return null;
	}

	/** Finds a node's parent by name, recursively inspecting children from the
	 * root
	 * @param rootNode the starting node
	 * @param target the parent we're looking for
	 * @return */
	public AreaNodeDefinition findParent(AreaNodeDefinition rootNode, String target) {
		if (rootNode != null && rootNode.name != null) {
			if (rootNode.children != null) {
				for (AreaNodeDefinition childNode : rootNode.children) {
					if (childNode.name.equals(target)) {
						return rootNode;
					} else {
						AreaNodeDefinition parent = find(childNode, target);
						if (parent != null) {
							return parent;
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name != null ? name : "";
	}

	/** @return an {@link Array} containing all nodes */
	public Array<AreaNodeDefinition> getNodes() {
		Array<AreaNodeDefinition> nodes = new Array<>();
		addNodes(nodes, root);
		return nodes;
	}
}
