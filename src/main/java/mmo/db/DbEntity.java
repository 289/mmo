package mmo.db;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jinshuai on 16/7/31.
 */
public abstract class DbEntity {

    protected static final byte NONE   = 0;
    protected static final byte INSERT = 1;
    protected static final byte UPDATE = 2;
    protected static final byte DELETE = 3;

    protected DbEntity mirrorEntity = null;
    protected boolean isMirror;
    protected byte mark;
    protected ReentrantLock mirrorLock = new ReentrantLock();

    protected abstract void initMirror();
    public abstract void update();

    protected void initMirror0(){
        isMirror = true;
        mark = NONE;
    }

    /**
     * 外部用mirrorLock同步调用
     * @param <E>
     * @return
     */
    protected <E extends DbEntity> E getMirror() {
        if (mirrorEntity == null) {
            initMirror();
        }
        return mirrorEntity.cast();
    }

    protected void resetMark() {
        mark = NONE;
    }

    protected void onUpdate() {
        mark = UPDATE;
    }

    protected void onInsert() {
        mark = INSERT;
    }

    protected void onDelete() {
        mark = DELETE;
    }

    public <T extends DbEntity> T cast() {
        return (T) this;
    }
}
