package mmo.util;


import com.alibaba.fastjson.JSON;

/**
 * @author Jin Shuai
 */
public class JsonUtil {

    /**
     * 通过json序列化、反序列化拷贝一个对象，返回当前类或者子类的新对象
     *
     * 建议初始化时使用，效率敏感的地方慎用
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copyObj(Object obj, Class<? extends T> clazz){
        if(obj == null || clazz == null){
            return null;
        }
        if(!obj.getClass().isAssignableFrom(clazz)){
            return null;
        }
        T retObj = null;
        try {
            String jsonStr = JSON.toJSONString(obj);
            retObj = JSON.parseObject(jsonStr, clazz);
        } catch (Exception e) {
        }
        return retObj;
    }

    public static <T> T copyObj(T obj){
        if(obj == null){
            return null;
        }
        return copyObj(obj, (Class<? extends T>) obj.getClass());
    }

}
