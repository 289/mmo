package mmo.db;

/**
 * Created by jinshuai on 16/7/31.
 */
public interface TransLog {
    void commit();
    void rollback();
}
