//package mmo.util;
//
//import com.zeus.qiumo.mobile.entity.Point;
//import com.zeus.qiumo.mobile.pb.common.BaseTypeProto;
//import com.zeus.qiumo.mobile.pb.common.PointMsgProto;
//import com.zeus.qiumo.mobile.pb.skill.SkillProto;
//import com.zeus.qiumo.mobile.scene.Direction;
//import com.zeus.qiumo.mobile.skill.SkillDamage;
//
//import java.util.Map;
//
///**
// * @author Jin Shuai
// */
//public class PbUtil {
//
//    public static SkillProto.DamageMsg toDamageMsg(SkillDamage skillDamage) {
//        SkillProto.DamageMsg.Builder builder = SkillProto.DamageMsg.newBuilder();
//        builder.setBeAttackedSpriteId(skillDamage.getBeAttackedSpriteId());
//        builder.setIsImmune(skillDamage.isImmune());
//        builder.setIsHit(skillDamage.isHit());
//        builder.setIsCrit(skillDamage.isCrit());
//        builder.setIsJumpDodge(skillDamage.isJumpDodge());
//        builder.setHpHurtVal(skillDamage.getHpHurtVal());
//        int index = 0;
//        if (skillDamage.getPropertyMap() != null && !skillDamage.getPropertyMap().isEmpty()) {
//            for (Map.Entry<Integer, Long> entry : skillDamage.getPropertyMap().entrySet()) {
//                builder.addPropertiesBuilder(index);
//                builder.getPropertiesBuilder(index).setKey(entry.getKey()).setVal(entry.getValue());
//                index++;
//            }
//        }
//        return builder.build();
//    }
//
//    public static Point pbToPoint(PointMsgProto.PointMsg pointMsg) {
//        return new Point(pointMsg.getX(), pointMsg.getY(), pointMsg.getZ());
//    }
//
//    public static Direction pbToDirection(PointMsgProto.DirectionMsg directionMsg) {
//        return new Direction(directionMsg.getX(), directionMsg.getY());
//    }
//
//    public static BaseTypeProto.IntMsg intToPb(int i) {
//        BaseTypeProto.IntMsg.Builder builder = BaseTypeProto.IntMsg.newBuilder();
//        builder.setVal(i);
//        return builder.build();
//    }
//
//    public static BaseTypeProto.IdCountMsg idCountToPb(int id, int count) {
//        BaseTypeProto.IdCountMsg.Builder builder = BaseTypeProto.IdCountMsg.newBuilder();
//        builder.setId(id);
//        builder.setCount(count);
//        return builder.build();
//    }
//}
