package net.alcuria.umbracraft.engine;

import net.alcuria.umbracraft.engine.Pathfinder.PathNode;

/** Handles calculating distances for the {@link Pathfinder}
 * @author Andrew Keturi */
public class Heuristic {

	/** Calculates distance between two nodes
	 * @param node1
	 * @param node2
	 * @return */
	public static int calculateCost(PathNode node1, PathNode node2) {
		return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
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
