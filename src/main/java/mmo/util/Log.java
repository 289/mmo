//package mmo.util;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author Jin Shuai
// *         <p/>
// *         日志工具类
// */
//public class Log {
//    private static Logger logger = LoggerFactory.getLogger("");
//    private static String thisClassName = Log.class.getName();
//    private static String msgSep = "\r\n";
//
//    /**
//     * 获得调用类的栈帧
//     *
//     * @param object
//     * @return
//     */
//    private static String getStackMsg(Object object) {
//        //获取当前线程的堆栈信息
//        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//        if (stackTraceElements == null) {
//            return "";
//        }
//
//        StackTraceElement targetStack = null;
//        for (int i = 0; i < stackTraceElements.length; i++) {
//            StackTraceElement var = stackTraceElements[i];
//            String className = var.getClassName();
//            //找到本类的首个栈帧，往后第二个栈帧为调用类的栈帧
//            if (thisClassName.equals(className)) {
//                int targetIndex = i + 2;
//                if (targetIndex < stackTraceElements.length) {
//                    targetStack = stackTraceElements[targetIndex];
//                }
//                break;
//            }
//        }
//        return (targetStack == null ? "" : targetStack.toString()) + msgSep + object.toString();
//    }
//
//    public static void debug(Object message) {
//        String object = getStackMsg(message);
//        logger.debug(object);
//    }
//
//    public static void debug(Object message, Throwable t) {
//        String object = getStackMsg(message);
//        logger.debug(object, t);
//    }
//
//    public static void info(Object message) {
//        String object = getStackMsg(message);
//        logger.info(object);
//    }
//
//    public static void info(Object message, Throwable t) {
//        String object = getStackMsg(message);
//        logger.info(object, t);
//    }
//
//    public static void warn(Object message) {
//        String object = getStackMsg(message);
//        logger.warn(object);
//    }
//
//    public static void warn(Object message, Throwable t) {
//        String object = getStackMsg(message);
//        logger.warn(object, t);
//    }
//
//    public static void error(Object message) {
//        String object = getStackMsg(message);
//        logger.error(object);
//    }
//
//    public static void error(Object message, Throwable t) {
//        String object = getStackMsg(message);
//        logger.error(object, t);
//    }
//
//}
