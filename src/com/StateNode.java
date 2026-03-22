package com;

class StateNode implements Comparable<StateNode> {
	public int runningCost;
	public int estimateCost;
	public SlidingGameState state;
	public DirectionListNode directionListNode;
	
	public StateNode(SlidingGameState state) {
		this.state = state;
		runningCost = 0;
		estimateCost = state.estimateCost();
		directionListNode = null;
	}

	private StateNode(StateNode previousState, Direction direction) {
		state = new SlidingGameState(previousState.state);
		if (!state.move(direction)) {
			throw new RuntimeException("Move is invalid.");
		}

		runningCost = previousState.runningCost + 1;
		estimateCost = state.estimateCost();
		directionListNode = new DirectionListNode(direction, previousState.directionListNode);
	}

	public StateNode tryMove(Direction direction) {
		// check to see if this move is not just an undo of the last one
		if (directionListNode != null && directionListNode.dir.isOpposite(direction)) {
			return null;
		}
		
		if (!state.canMove(direction)) {
			return null;
		}

		return new StateNode(this, direction);
	}
	
	public int totalCost() {
		return runningCost + estimateCost;
	}

	@Override
	public int compareTo(StateNode arg0) {
		return totalCost() - arg0.totalCost();
	}
}

