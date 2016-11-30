package mmo.db;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by jinshuai on 16/7/31.
 */
public abstract class DbEntity {

    protected static final byte UN_LOCK = 0;
    protected static final byte LOCK = 1;

    protected Set<String> dirtyProps = new HashSet<>();
    protected AtomicReference<Byte> isLock = new AtomicReference<>(UN_LOCK);
    public abstract void save();

    public abstract <T extends DbEntity> T copy(Set<String> dirtyProps);

    public void markPropDirty(String prop){
        try {
            while (!isLock.compareAndSet(UN_LOCK, LOCK)) {
                //spin
            }
            dirtyProps.add(prop);
        } finally {
            isLock.set(UN_LOCK);
        }
    }

    public void removePropDirty(String prop){
        try {
            while (!isLock.compareAndSet(UN_LOCK, LOCK)) {
                //spin
            }
            dirtyProps.remove(prop);
        } finally {
            isLock.set(UN_LOCK);
        }
    }

    public <T extends DbEntity> T cast() {
        return (T) this;
    }
}
