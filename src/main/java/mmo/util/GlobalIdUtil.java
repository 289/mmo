//package mmo.util;
//
//import com.zeus.qiumo.mobile.mybatis.MapperMgr;
//import com.zeus.qiumo.mobile.mybatis.role.dao.GenRoleIdMapper;
//import com.zeus.qiumo.mobile.mybatis.role.entity.GenRoleId;
//import com.zeus.qiumo.mobile.server.GameServerConfig;
//
//import java.util.UUID;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//
///**
// * @author Jin Shuai
// */
//public class GlobalIdUtil {
//
//    private static long getIncreId() {
//        try {
//            GenRoleIdMapper mapper = MapperMgr.getRoleMapper(GenRoleIdMapper.class);
//            GenRoleId genRoleId = new GenRoleId();
//            mapper.insert(genRoleId);
//            return genRoleId.getId().longValue();
//        } catch (Exception e) {
//            Log.error("get increment id error", e);
//            return -1;
//        }
//    }
//
//    public static long getNextRoleId() {
//        return getGlobalRoleId(GameServerConfig.getPfId(), GameServerConfig.getServerId(), getIncreId());
//    }
//
//    public static String getNextAccountId() {
//        return getUUID();
//    }
//
//    public static String getUUID() {
//        String id = UUID.randomUUID().toString().replaceAll("-", "");
//        return id;
//    }
//
//    public static String getNextItemId() {
//        return UUID.randomUUID().toString().replaceAll("-", "");
////        return 0;
//    }
//
//    /**
//     * 生成全局唯一id
//     *
//     * platform     10bit
//     * serverId     13bit
//     * autoIncreId  40bit
//     *
//     * @param pfId
//     * @param serverId
//     * @param autoIncreId
//     * @return
//     */
//    private static long getGlobalRoleId(int pfId, int serverId, long autoIncreId) {
//        if (pfId > Math.pow(2, 10) - 1 || pfId < 0
//                || serverId > Math.pow(2, 13) - 1 || serverId < 1
//                || autoIncreId > Math.pow(2, 40) - 1 || autoIncreId < 1) {
//            Log.error(String.format("generate role id error, pfId = %d, serverId = %d, autoIncreId = %d", pfId, serverId, autoIncreId));
//            return -1;
//        }
//        return (autoIncreId << 23) + (serverId << 10) + pfId;
//    }
//
//    public static void main(String[] args) {
////        System.out.println(getGlobalRoleId(0, 8011, 5));
////        System.out.println(Math.pow(2, 6) - 1 );
//        System.out.println(Math.pow(2, 14) - 1);
//        long xx = (long) (Math.pow(2, 44) - 1);
//        System.out.println(xx);
////        long x = Integer.MAX_VALUE;
////        System.out.println( x);
////        System.out.println((1 << 16) + (1 << 6) + 1);
//        System.out.println(Math.pow(2, 64));
//        System.out.println(Long.MAX_VALUE);
//        System.out.println(Integer.MAX_VALUE);
//        System.out.println("4294967295");
//        Executors.newCachedThreadPool();
//        Executors.newFixedThreadPool(10);
//        Executors.newSingleThreadExecutor();
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
////        executorService.scheduleAtFixedRate();
////        executorService.scheduleWithFixedDelay();
//    }
//}
