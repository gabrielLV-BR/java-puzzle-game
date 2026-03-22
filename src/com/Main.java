package com;

import javax.swing.SwingUtilities;

public final class Main {
	public static void main(String[] args) {
		SlidingGameSolver solver = new SlidingGameSolver();
		SlidingGameState state = new SlidingGameState(3, 3);
		state.shuffle(1);
	
		SwingUtilities.invokeLater(() -> {
			SlidingGameUI SlidingGameUI = new SlidingGameUI(state, solver);
			SlidingGameUI.show();
		});
	}
}
