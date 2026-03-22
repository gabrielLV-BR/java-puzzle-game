package com;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

class DirectionListItem {
    final Direction direction;
    private boolean isComplete;

    public DirectionListItem(Direction dir) {
        direction = dir;
        isComplete = false;
    }

    public void complete() {
        isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }
}

public class StepsViewer implements MoveListener {
    JFrame frame;
    JList<String> list;

    int listenerId;
    int lastComplete;
    ArrayList<DirectionListItem> listItems;

    public StepsViewer(List<Direction> steps, MoveBroadcaster broadcaster) {
        lastComplete = 0;

        this.listItems = new ArrayList<DirectionListItem>(
            steps
            .stream()
            .map(DirectionListItem::new)
            .toList());


        listenerId = broadcaster.subscribe(this);

        list = new JList<>();
        list.setModel(new AbstractListModel<String>() {
            @Override
            public String getElementAt(int index) {
                return listItems.get(index).direction.toString();
            }

            @Override
            public int getSize() {
                return listItems.size();
            }
        });

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (listItems.get(index).isComplete()) {
                    c.setBackground(Color.green);
                }

                return c;
            }
        });

        JLabel label = new JLabel("Passos");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(list);

        frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                broadcaster.unsubscribe(listenerId);
                frame.dispose();
            }
        });
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void show() {
        frame.setVisible(true);
    }

    @Override
    public void onMove(Direction direction) {
        if (lastComplete >= listItems.size()) {
            return;
        }

        DirectionListItem lastItem = listItems.get(lastComplete);
        
        if (lastItem.direction == direction.getOpposite()) {
            lastItem.complete();
            list.updateUI();
            lastComplete++;
        }
    }
}
