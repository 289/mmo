package mmo.db;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jinshuai on 16/7/31.
 */
public abstract class DbEntity {
    public static enum Mark {
        NONE, INSERT, UPDATE, DELETE;
    }

    protected DbEntity mirror = null;
    protected boolean isMirror;

    @SuppressWarnings("unchecked")
    protected <E extends DbEntity> E getMirror(){
        if(mirror == null){
            initMirror();
        }
        return (E) mirror;
    }

    public abstract void initMirror();
    public abstract void update();

    private AtomicReference<Mark> op = new AtomicReference(Mark.NONE);

    public Mark getOp() {
        return (Mark) this.op.get();
    }

    private void onOperation(Mark op) {
        switch (op){
            case NONE:
                break;
            case INSERT:
                this.op.compareAndSet(Mark.NONE, Mark.INSERT);
                break;
            case UPDATE:
                this.op.compareAndSet(Mark.NONE, Mark.UPDATE);
                break;
            case DELETE:
                this.op.set(Mark.DELETE);
                break;
        }
    }

    public void restMark(){
        op.set(Mark.NONE);
    }

    public void onUpdate() {
        onOperation(Mark.UPDATE);
    }

    public void onInsert() {
        onOperation(Mark.INSERT);
    }

    public void onDelete() {
        onOperation(Mark.DELETE);
    }

    public <T extends DbEntity> T cast() {
        return (T) this;
    }
}
