package mmo.db.trans;


import java.util.Deque;
import java.util.LinkedList;

public final class Transaction {

    private static ThreadLocal<Transaction> threadlocal = new ThreadLocal<>();

    private Deque<TransLog> logQueue = new LinkedList<>();

    public void addTRecord(TransLog record) {
        logQueue.offer(record);
    }

    public static final void begin() {
        threadlocal.set(new Transaction());
    }

    public static final Transaction current() {
        return threadlocal.get();
    }

    public static final void destroy() {
        threadlocal.set(null);
    }

    public static final void commit(){
        current().logQueue.forEach(transLog -> transLog.commit());
    }

    public static final void rollBack(){
        Deque<TransLog> logDeque = current().logQueue;
        TransLog log;
        while ((log = logDeque.pollLast()) != null){
            log.rollback();
        }
    }

}
