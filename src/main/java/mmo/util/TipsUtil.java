//package mmo.util;
//
//import com.zeus.qiumo.mobile.map.GameMap;
//import com.zeus.qiumo.mobile.net.message.NetMessage;
//import com.zeus.qiumo.mobile.pb.common.TipsMsgProto;
//import com.zeus.qiumo.mobile.role.Role;
//import com.zeus.qiumo.mobile.server.SubLineMgr;
//import com.zeus.qiumo.mobile.server.SubLineServer;
//import com.zeus.qiumo.mobile.server.protocol.ServerProtocol;
//import com.zeus.qiumo.mobile.server.tips.SystemTips;
//
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//
///**
// * @author Jin Shuai
// */
//public class TipsUtil {
//
//    public static enum TipsEnum {
//        /**
//         * 未指定区域，客户端默认区域
//         */
//        DEFAULT(0),
//        /**
//         * 打印在聊天框内
//         */
//        LEFT_CHAT(1),
//        /**
//         * 中间明显提示区(角色脚下)
//         */
//        FLOOT_TIPS(2),
//        /**
//         * 右侧系统提示(收益栏)
//         */
//        RIGHT_TIPS(3),
//        /**
//         * 系统公告(滚动)(.......修正为  A1区，系统公告提示区域)
//         */
//        SYS_BROADCAST(4),
//        /**
//         * 玩家信息公告(...............修正为A2区，玩家信息公告提示区域)
//         */
//        PLAYERINFO_BROADCAST(5),
//        /**
//         * 提示信息(人物头顶)
//         */
//        HEAD_TIPS(6),
//        /**
//         * 弹出提示(由于6、7的提示信息统一发送到B区，所以把发7的修改为发6)
//         */
//        ALERT_TIPS(7),
//        /**
//         * 副本内提示信息
//         */
//        INSTANCE_TIPS(8),
//
//        /**
//         * 聊天框指定频道显示
//         */
//        ASSIGN_CHANNEL(9),;
//
//        public final int place;
//
//        TipsEnum(int place) {
//            this.place = place;
//        }
//    }
//
//    public static NetMessage createTipsMsg(int tipsCode, String... vars) {
//        return createTipsMsg(TipsEnum.DEFAULT, tipsCode, vars);
//    }
//
//    public static NetMessage createTipsMsg(String content, String... vars) {
//        return createTipsMsg(content, TipsEnum.DEFAULT, SystemTips.TIPS_WITH_CONTENT, vars);
//    }
//
//    public static NetMessage createTipsMsg(TipsEnum place, int tipsCode, String... vars) {
//        return createTipsMsg("", place, tipsCode, vars);
//    }
//
//    public static NetMessage createTipsMsg(String content, TipsEnum place, String... vars) {
//        return createTipsMsg(content, place, SystemTips.TIPS_WITH_CONTENT, vars);
//    }
//
//    /**
//     * 创建原始通用消息
//     *
//     */
//    private static NetMessage createTipsMsg(String content, TipsEnum position, int codeNum, String... vars) {
//        TipsMsgProto.TipsMsg.Builder builder = TipsMsgProto.TipsMsg.newBuilder();
//        builder.setTipsType(position.place);
//        builder.setCode(codeNum);
//        builder.addAllTipsParams(Arrays.asList(vars));
//        if (position.place == SystemTips.TIPS_WITH_CONTENT) {
//            builder.setContent(content);
//        }
//        NetMessage message = NetMessage.build(ServerProtocol.Server.TIPS, builder.build());
//        return message;
//    }
//
//
//    /**
//     * 打印在聊天框内通用消息；
//     *
//     * @param codeNum
//     * @return
//     */
//    public static NetMessage createLeftChat(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.LEFT_CHAT, codeNum, vars);
//    }
//
//    /**
//     * 中间提示区(角色脚下)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createFlootTipMsg(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.FLOOT_TIPS, codeNum, vars);
//    }
//
//    /**
//     * 右侧系统提示(收益栏)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createRightTipMsg(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.RIGHT_TIPS, codeNum, vars);
//    }
//
//
//    /**
//     * 系统公告(滚动)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createSysBroadcastMsg(String content, String... vars) {
//        return createTipsMsg(content, TipsEnum.SYS_BROADCAST, vars);
//    }
//
//    /**
//     * GM公告(滚动)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createGMBroadcastMsg(String content, String... vars) {
//        return createTipsMsg(content, TipsEnum.PLAYERINFO_BROADCAST, vars);
//    }
//
//    /**
//     * 提示信息(人物头顶)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createHeadTipMsg(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.HEAD_TIPS, codeNum, vars);
//    }
//
//    /**
//     * 弹出提示（Alert提示）
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createAlertTipMsg(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.ALERT_TIPS, codeNum, vars);
//    }
//
//    /**
//     * 副本内信息提示
//     */
//    public static NetMessage createInstanceTipMsg(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.INSTANCE_TIPS, codeNum, vars);
//    }
//    /**
//     * ======================================================= 发送通用提示消息
//     * =======================================================
//     */
//
//    /**
//     * 中间明显提示区(角色脚下)================  C 区   ================
//     */
//    public static void sendFlootTipMessage(int codeNum, Collection<Role> roleCollection, String... vars) {
//        for (Role role : roleCollection) {
//            sendFlootTipMessage(codeNum, role, vars);
//        }
//    }
//
//    /**
//     * 中间明显提示区(角色脚下)================  C 区   ================
//     */
//    public static void sendFlootTipMessage(int codeNum, Role role, String... vars) {
//        if (role != null) {
//            role.sendMsg(createFlootTipMsg(codeNum, vars));
//        }
//    }
//
//    /**
//     * 右侧系统提示(收益栏)================  E 区   ================
//     */
//    public static void sendRightTipMessage(int codeNum, Collection<Role> roleCollection, String... vars) {
//        for (Role role : roleCollection) {
//            sendRightTipMessage(codeNum, role, vars);
//        }
//    }
//
//    /**
//     * 右侧系统提示(收益栏)================  E 区   ================
//     */
//    public static void sendRightTipMessage(int codeNum, Role role, String... vars) {
//        if (role != null) {
//            role.sendMsg(createRightTipMsg(codeNum, vars));
//        }
//    }
//
//    /**
//     * 系统公告(滚动)================  A1 区   ================
//     */
//    public static void sendSysBroadcastMessage(String content, String... vars) {
//        sendBroadCast2AllServer(createSysBroadcastMsg(content, vars));
//    }
//
//    /**
//     * GM公告
//     */
//    public static void sendGMBroadcastMessage(String content) {
//        sendBroadCast2AllServer(createGMBroadcastMsg(content));
//    }
//
//    /**
//     * 玩家信息公告================  A2 区   ================
//     */
//    public static void sendPlayerInfoBroadcastMessage(int codeNum, String... vars) {
//        sendBroadCast2AllServer(createPlayerInfoBroadcastMsgArr(codeNum, vars));
//    }
//
//    /**
//     * 系统公告(滚动)
//     *
//     * @param codeNum
//     * @param vars
//     * @return
//     */
//    public static NetMessage createPlayerInfoBroadcastMsgArr(int codeNum, String... vars) {
//        return createTipsMsg(TipsEnum.PLAYERINFO_BROADCAST, codeNum, vars);
//    }
//
//    /**
//     * 特定地图系统公告
//     */
//
//    /**
//     * 特定线路特定地图系统公告================  A2 区   ================
//     */
//
//    /**
//     * 实现全服广播
//     *
//     * @param msg
//     */
//    private static void sendBroadCast2AllServer(NetMessage msg) {
//        List<Role> allOnlineRoles = SubLineMgr.getAllOnlineRole();
//        for (Role role : allOnlineRoles) {
//            role.sendMsg(msg);
//        }
//    }
//
//    /**
//     * 广播给特定线路特定地图上玩家
//     *
//     * @param mapId
//     * @param msg
//     */
//    private static void sendBroadCast2Map(int mapId, int vlineId, NetMessage msg) {
//        SubLineServer vLineServer = SubLineMgr.getLineById(vlineId);
//        GameMap map = vLineServer.getMapById(mapId);
//        for (Role r : map.getMapRoles()) {
//            r.sendMsg(msg);
//        }
//    }
//
//    /**
//     * 广播给特定地图上玩家
//     *
//     * @param mapId
//     * @param msg
//     */
//    private static void sendBroadCast2Map(int mapId, NetMessage msg) {
//        Collection<SubLineServer> vLineServerCollection = SubLineMgr.getLineServersList();
//        for (SubLineServer vLineServer : vLineServerCollection) {
//            sendBroadCast2Map(mapId, vLineServer.getLineId(), msg);
//        }
//    }
//
//    /**
//     * B提示信息(人物头顶)
//     */
//    public static void sendHeadTipMessage(int codeNum, Collection<Role> roleCollection, String... vars) {
//        for (Role role : roleCollection) {
//            sendHeadTipMessage(codeNum, role, vars);
//        }
//    }
//
//    //================  B 区   ================
//    public static void sendHeadTipMessage(int codeNum, Role role, String... vars) {
//        if (role != null) {
//            role.sendMsg(createHeadTipMsg(codeNum, vars));
//        }
//    }
//
//    /**
//     * D副本内信息提示
//     */
//    public static void sendInstanceTipMessage(int codeNum, Role role, String... vars) {
//        if (role != null) {
//            role.sendMsg(createInstanceTipMsg(codeNum, vars));
//        }
//    }
//
//    /**
//     * 弹出提示（Alert提示）
//     */
//    public static void sendAlertTipMessage(int codeNum, Collection<Role> roleCollection, String... vars) {
//        for (Role role : roleCollection) {
//            sendHeadTipMessage(codeNum, role, vars);
//        }
//    }
//
//    //================  B区   ================
//    public static void sendAlertTipMessage(int codeNum, Role role, String... vars) {
//        if (role != null) {
//            role.sendMsg(createHeadTipMsg(codeNum, vars));
//        }
//    }
//
//    public static void sendInstanceTipMessageToAll(int battleWillEnd, Set<Role> allPalyerOnInstance, String... var) {
//        if (allPalyerOnInstance.size() <= 0) {
//            return;
//        }
//        for (Role role : allPalyerOnInstance) {
//            sendInstanceTipMessage(battleWillEnd, role, var);
//        }
//    }
//
//}
