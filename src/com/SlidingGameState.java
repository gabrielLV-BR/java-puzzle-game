package com;

import java.util.Arrays;
import java.util.Random;

public class SlidingGameState {
	final int columns;
	final int rows;

	private boolean isComplete;
	private int zeroIndex;
	
	public char[] numbers;
	
	public SlidingGameState(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		numbers = new char[columns * rows];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = (char)i;
		}
		
		zeroIndex = 0;
	}
	
	public void shuffle(int complexity) {
		Direction direction;
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		
		for (int i = 0; i < complexity; i++) {
			do {
				int index = random.nextInt(Direction.AllDirections.length);
				direction = Direction.AllDirections[index];
			} while (!canMove(direction));

			move(direction);
		} 
	}

	public SlidingGameState(SlidingGameState other) {
		columns = other.columns;
		rows = other.rows;
		numbers = new char[other.numbers.length];
		zeroIndex = other.zeroIndex;
		isComplete = other.isComplete;
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = other.numbers[i];
		}
	}

	public int columns() {
		return columns;
	}

	public int rows() {
		return rows;
	}
	
	// pretty dumb
	// makes it so that we visit every possible state 
	// with no distinction for which gets us closer to the end
	// public int estimateCost() {
	// 	return 0;
	// }
	
	// pretty dumb but a little bit better
	// gives us the number of numbers in the wrong spot
	// public int estimateCost() {
	// 	int differences = 0;
		
	// 	for (int i = 0; i < numbers.length - 1; i++) {
	// 		if (numbers[i] != (i + 1)) {
	// 			differences++;
	// 		}
	// 	}

	// 	return differences;
	// }

	// smarter
	// gives us the total (manhattan) distance of each number to its correct spot
	// costlier but a better heuristic
	public int estimateCost() {
		int totalDistance = 0;

		for (int number = 1; number < numbers.length; number++) {
			int idealIndex = number - 1;
			int actualIndex = getIndexOf(number);

			totalDistance += getIndicesDistance(idealIndex, actualIndex);
		}

		totalDistance += getIndicesDistance(zeroIndex, numbers.length - 1);

		return totalDistance;
	}

	public int getZeroIndex() {
		return zeroIndex;
	}

	public int getIndexOf(int number) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == number) {
				return i;
			}
		}

		return -1;
	}
	
	public boolean canMove(Direction dir) {
		return getIndexAfterMove(getZeroIndex(), dir) != -1;
	}

	public int cellAt(int column, int row) {
		int index = column + row * rows;
		return numbers[index];
	}
	
	public boolean move(Direction direction) {
		int newIndex = getIndexAfterMove(zeroIndex, direction);
		
		if (newIndex == -1) {
			return false;
		}
		
		char aux = numbers[newIndex];
		numbers[newIndex] = numbers[zeroIndex];
		numbers[zeroIndex] = aux;

		zeroIndex = newIndex;
		checkIsComplete();
		return true;
	}
	
	public boolean isComplete() {
		return isComplete;
	}
	
	private int getIndexAfterMove(int zeroIndex, Direction direction) {
		int dx = 0, dy = 0;
		
		switch (direction) {
		case Up: dy = -1; break;
		case Down: dy = 1; break;
		case Left: dx = -1; break;
		case Right: dx = 1; break;
		}
		
		int zeroColumn = zeroIndex % columns;
		int zeroRow = (zeroIndex - zeroColumn) / rows;
		
		zeroColumn += dx;
		zeroRow += dy;
		
		if (zeroColumn < 0 || zeroColumn >= columns || zeroRow < 0 || zeroRow >= rows) {
			return -1;
		}
		
		return zeroColumn + zeroRow * columns;
	}
	
	private void checkIsComplete() {
		isComplete = false;
		if (numbers[numbers.length - 1] != 0) {
			return;
		}
		
		for (int i = 0; i < numbers.length - 1; i++) {
			if (numbers[i] != (i+1)) {
				return;
			}
		}
		
		isComplete = true;
	}

	public int getIndicesDistance(int index1, int index2) {
		int index1Column = index1 % columns;
		int index1Row = (index1 - index1Column) / rows;
		
		int index2Column = index2 % columns;
		int index2Row = (index2 - index2Column) / rows;

		int colDistance = Math.abs(index2Column - index1Column);
		int rowDistance = Math.abs(index2Row - index1Row);

		if (rowDistance > colDistance) {
			int aux = colDistance;
			colDistance = rowDistance;
			rowDistance = aux;
		}

		return colDistance - rowDistance;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof SlidingGameState)) {
			return false;
		}
		
		SlidingGameState otherState = (SlidingGameState)other;
		return Arrays.equals(numbers, otherState.numbers);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(numbers);
	}
}
