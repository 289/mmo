package mmo;


import mmo.db.MybatisMapper;
import mmo.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jin Shuai
 */
public class ClazzMgr {
    private static Set<Class<? extends MybatisMapper>> mapperClasses = new HashSet<>();

    public static void init(){
        Set<Class<?>> classes = ClassUtil.getClasses(Bootstrap.class.getPackage());
        for (Class<?> clazz : classes) {
            if(MybatisMapper.class.isAssignableFrom(clazz) && clazz != MybatisMapper.class){
                Class<? extends MybatisMapper> mapperClazz = (Class<? extends MybatisMapper>) clazz;
                mapperClasses.add(mapperClazz);
            }
        }
    }

    public static Set<Class<? extends MybatisMapper>> getMapperClasses() {
        Set<Class<? extends MybatisMapper>> set = new HashSet<>();
        set.addAll(mapperClasses);
        return set;
    }

}
