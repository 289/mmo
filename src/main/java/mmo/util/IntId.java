package mmo.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IntId {
    private AtomicInteger currentId = new AtomicInteger(0);
    private int fromId = 0;
    private int maxId = Integer.MAX_VALUE;

    public IntId() {
    }

    public IntId(int fromId) {
        this.fromId = fromId;
        this.currentId = new AtomicInteger(fromId);
    }

    public IntId(int fromId, int toId) {
        this.fromId = fromId;
        this.currentId = new AtomicInteger(fromId);
        this.maxId = toId;
    }

    public int getNextId() {
        if (currentId.get() == maxId) {
            currentId.compareAndSet(maxId, fromId);
        }
        return currentId.getAndIncrement();
    }
}
