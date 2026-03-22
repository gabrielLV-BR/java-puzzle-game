package com;

public enum Direction {
	Up, Left, Right, Down;
	
	public static final Direction[] AllDirections = new Direction[] { Direction.Up, Direction.Left, Direction.Right, Direction.Down };

	@Override
	public String toString() {
		switch (this) {
		case Up: return "Up";
		case Left: return "Left";
		case Right: return "Right";
		case Down: return "Down";
		}
		
		return null;
	}
	
	public Direction getOpposite() {
		switch (this) {
		case Up: return Down;
		case Left: return Right;
		case Right: return Left;
		case Down: return Up;
		}
		
		throw new RuntimeException("Invalid enum");
	}
	
	public boolean isOpposite(Direction other) {
		return this.getOpposite() == other;
	}
}
