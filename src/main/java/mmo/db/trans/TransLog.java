package mmo.db.trans;

/**
 * Created by jinshuai on 16/7/31.
 */
public interface TransLog {
    void commit();
    void rollback();
}
