package mmo.db;

/**
 * Created by jinshuai on 16/7/31.
 */
public abstract class DbEntity {
    public enum Mark {
        NONE, INSERT, UPDATE, DELETE;
    }

    protected DbEntity mirrorEntity = null;
    protected boolean isMirror;
    protected volatile Mark mark = null;
    protected Object mirrorLock = new Object();

    protected abstract void initMirror();
    public abstract void update();

    protected void initMirror0(){
        isMirror = true;
        mark = Mark.NONE;
    }

    protected void clearMirror(){
        mirrorEntity = null;
    }

    protected <E extends DbEntity> E getMirror() {
        if (mirrorEntity == null) {
            initMirror();
        }
        return mirrorEntity.cast();
    }

    protected void resetMark() {
        mark = Mark.NONE;
    }

    protected void onUpdate() {
        mark = Mark.UPDATE;
    }

    protected void onInsert() {
        mark = Mark.INSERT;
    }

    protected void onDelete() {
        mark = Mark.DELETE;
    }

    public <T extends DbEntity> T cast() {
        return (T) this;
    }
}
