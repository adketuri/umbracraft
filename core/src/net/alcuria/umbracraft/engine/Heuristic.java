package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.engine.Pathfinder.PathNode;

/** Handles calculating distances for the {@link Pathfinder. Adjacent nodes have a distance of 10 and diagonal nodes have a distance of 14.
 * @author Andrew Keturi */
public class Heuristic {

	/** Calculates distance between two nodes
	 * @param node1
	 * @param node2
	 * @return */
	public static int calculateCost(PathNode node1, PathNode node2) {
		int dX = Math.abs(node1.x - node2.x);
		int dY = Math.abs(node1.y - node2.y);
		if (dX > dY) {
			return 14 * dY + 10 * (dX - dY);
		} else {
			return 14 * dX + 10 * (dY - dX);
		}
	}

	/** Calculates the F cost between two nodes passing through current.
	 * @param source
	 * @param destination
	 * @param current
	 * @return */
	public static int calculateFCost(PathNode source, PathNode destination, PathNode current) {
		return calculateCost(source, current) + calculateCost(destination, current);
	}

}
