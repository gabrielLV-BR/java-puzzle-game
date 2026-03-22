package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class SlidingGameSolver {
	private final HashSet<StateNode> visitedStates;
	private final PriorityQueue<StateNode> queue;

	public SlidingGameSolver() {
		visitedStates = new HashSet<>();
		queue = new PriorityQueue<>();
	}

	public int getVisitedStates() {
		return visitedStates.size();
	}

	public List<Direction> solve(SlidingGameState state) {
		visitedStates.clear();
		queue.clear();

		StateNode result = solveInternal(new StateNode(state));
		
		DirectionListNode directionNode = result.directionListNode;
		
		// accumulate directions
		ArrayList<Direction> directions = new ArrayList<>();
		while (directionNode != null) {
			directions.add(directionNode.dir);
			directionNode = directionNode.previous;
		}

		return directions.reversed();
	}
	
	private StateNode solveInternal(StateNode startingNode) {
		queue.add(startingNode);

		while(!queue.isEmpty()) {
			StateNode node;
			
			do {
				node = queue.poll();
			} while(visitedStates.contains(node));
			
			if (node == null) {
				break;
			}
			
			// win scenario
			if (node.state.isComplete()) {
				return node;
			}
			
			// visit others
			visitedStates.add(node);
			
			for (Direction dir: Direction.AllDirections) {
				StateNode newState = node.tryMove(dir);
				
				if (newState != null) {
					queue.add(newState);
				}
			}
		}		
		return null;
	}
}
