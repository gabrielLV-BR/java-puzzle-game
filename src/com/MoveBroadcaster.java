package com;

public interface MoveBroadcaster {
    public int subscribe(MoveListener listener);
    public void unsubscribe(int id);
}
