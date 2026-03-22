package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class SlidingGameUI implements KeyListener, MoveBroadcaster {
	private JFrame frame;
	private JTable table;
	private JLabel completeLabel;

	private final ArrayList<MoveListener> listeners;
	private final SlidingGameState state;
	
	public SlidingGameUI(SlidingGameState state, SlidingGameSolver solver) {
		this.state = state;
		listeners = new ArrayList<>();

		frame = new JFrame("Sliding Puzzle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		
		JPanel panel = new JPanel();

		TableModel dataModel = new AbstractTableModel() {
		    public int getColumnCount() { return state.columns; }
		    public int getRowCount() { return state.rows; }
		    public Object getValueAt(int row, int col) {
		    	int num = state.cellAt(col, row);
		    	
		    	if (num == 0) {
		    		return "";
		    	}
		    	
		    	return num;
	    	}
		};

		table = new JTable(dataModel);
		Font font = table.getFont();
		font = font.deriveFont(30.0f);
		table.setFont(font);
		table.setRowHeight(100);
		table.addKeyListener(this);
		
		JButton solveButton = new JButton();
		solveButton.addActionListener((e) -> {
			List<Direction> solveSteps = solver.solve(state);
			
			System.out.println("Solved in " + solver.getVisitedStates() + " steps!");

			StepsViewer viewer = new StepsViewer(solveSteps, this);
			viewer.show();
		});
		solveButton.setText("Solve");
		
		JButton distanceButton = new JButton();
		distanceButton.addActionListener((e) -> {
			int row = table.getSelectedRow();
			int column = table.getSelectedColumn();

			if (row == -1 || column == -1) {
				System.out.println("Selecione um número.");
				return;
			}

			Object value = table.getValueAt(row, column);
			int numValue = 0, idealIndex = state.numbers.length - 1;

			if (value instanceof Integer) {
				numValue = (Integer)value;
				idealIndex = numValue - 1;
			}

			System.out.println(
				"Número " + numValue + 
				" tem distâncie de " +
				state.getIndicesDistance(state.getIndexOf(numValue), idealIndex));
		});
		distanceButton.setText("Distance");
		
		completeLabel = new JLabel("Puzzle completo!");
		completeLabel.setVisible(false);
		completeLabel.setFont(font);
		completeLabel.setForeground(Color.green);

		JPanel bigPanel = new JPanel();
		bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
		bigPanel.add(panel);
		bigPanel.add(completeLabel);

		panel.add(table);
		panel.add(solveButton);
		panel.add(distanceButton);

		frame.add(bigPanel);
	}

	private void move(Direction direction) {
		state.move(direction);
		update();

		for (MoveListener listener : listeners) {
			listener.onMove(direction);
		}
	}
	
	private void update() {
		table.updateUI();
		completeLabel.setVisible(state.isComplete());
	}
	
	public void show() {
		frame.setVisible(true);
	}

	public SlidingGameState getState() {
		return new SlidingGameState(state);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		switch (code) {
		case KeyEvent.VK_LEFT: move(Direction.Right); break;
		case KeyEvent.VK_RIGHT: move(Direction.Left); break;
		case KeyEvent.VK_UP: move(Direction.Down); break;
		case KeyEvent.VK_DOWN: move(Direction.Up); break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public int subscribe(MoveListener listener) {
		listeners.add(listener);
		
		int id = listeners.size() - 1;
		System.out.println("Subscribed listener " + id);
		return id;
	}

	@Override
	public void unsubscribe(int id) {
		System.out.println("Unsubscribed listener " + id);
		listeners.remove(id);
	}
}
