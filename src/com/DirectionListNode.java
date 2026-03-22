package com;

class DirectionListNode {
	public Direction dir;
	public DirectionListNode previous;

	public DirectionListNode(Direction dir) {
		this.dir = dir;
		previous = null;
	}

	public DirectionListNode(Direction dir, DirectionListNode prev) {
		this.dir = dir;
		this.previous = prev;
	}
}
