package mmo.db;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Shuai
 */
public class GameDataMgr {
    private static Map<Class<?>, List> dataMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Class<?>, Map<Object, Object>> priKeyDataMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Map<Object, List>> otherKeyDataMap = new ConcurrentHashMap<>();

    private static final String LOAD_DATA_METHD = "selectByExample";
    private static final String LOAD_BLOB_DATA_METHD = "selectByExampleWithBLOBs";

    public static void init() {
        loadData();
    }




    private static void loadData() {
//        for (MybatisMapper mybatisMapper : MapperMgr.getMapperByDb(DBEnvironment.GAMEDATA)) {
//            try {
//                Method targetMethod = getLoadDataMethod(mybatisMapper.getClass());
//                if (targetMethod == null) {
//                    continue;
//                }
//                List result = (List) targetMethod.invoke(mybatisMapper, (Object) null);
//                if (CollectionUtil.isNull(result)) {
//                    continue;
//                }
//                Class dataType = result.get(0).getClass();
//                if (parseClassMap.containsKey(dataType)) {
//                    Class<? extends DataParser> parserClazz = parseClassMap.get(dataType);
//                    result = parseData(result, parserClazz);
//                }
//                if (CollectionUtil.isNotNull(result)) {
//                    dataMap.put(result.get(0).getClass(), result);
//                }
//            } catch (Exception e) {
//                Log.error("load data error, info : " + mybatisMapper.getClass().getName(), e);
//            }
//        }
//        Log.info("game data load over");
    }

    /**
     * 获得一个mapper类的加载数据的方法
     * 优先找 selectByExampleWithBLOBs 方法，找不到则找 selectByExample 方法，selectByExample 方法一定存在
     *
     * @param mapperClazz
     * @return
     */
    private static Method getLoadDataMethod(Class<? extends MybatisMapper> mapperClazz){
        Method targetMethod = null;
        for (Method method : mapperClazz.getDeclaredMethods()) {
            if (method.getName().equals(LOAD_BLOB_DATA_METHD)) {
                targetMethod = method;
                break;
            }
            if (method.getName().equals(LOAD_DATA_METHD)) {
                targetMethod = method;
            }
        }
        return targetMethod;
    }

    /**
     * 按所给类型转换数据
     *
     * @param dataList
     * @param clazz
     * @param <E>
     * @return
     */


    private static Method getColValMethod(Class clazz, String columnName) {
        if (clazz == null || StringUtils.isBlank(columnName)) {
            return null;
        }
        String methodName = "get" + columnName;
        for (Method method : clazz.getMethods()) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        return null;
    }


}
