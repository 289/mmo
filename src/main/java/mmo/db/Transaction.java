package mmo.db;


import java.util.Deque;
import java.util.LinkedList;

public final class Transaction {

    /**
     * 结束事务，释放所有锁并且清除，清除wrapper。
     */
//    private void finish() {
//        wrappers.clear();
//    }

    private static ThreadLocal<Transaction> threadlocal = new ThreadLocal<>();
//    final Map<LogKey, Object> wrappers = new HashMap<>();

    private Deque<TransLog> logQueue = new LinkedList<>();

    public boolean addLog(TransLog log) {
        return logQueue.offer(log);
    }

    public static final void begin() {
        threadlocal.set(new Transaction());
    }

    public static final Transaction current() {
        return threadlocal.get();
    }

    public static final void destroy() {
//        current().wrappers.clear();
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
