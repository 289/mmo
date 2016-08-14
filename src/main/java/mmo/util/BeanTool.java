package mmo.util;

import java.lang.reflect.Method;
import java.util.*;

public class BeanTool {
    @SuppressWarnings("unchecked")
    public static <K, V, T, L> Map<K, V> listToMap(List<L> list, String properties, Class<T> mapclass) {
        try {
            Map<K, V> hashmap = (Map<K, V>) mapclass.newInstance();
            for (Iterator<L> iter = list.iterator(); iter.hasNext(); ) {
                Object item = iter.next();
                try {
                    Object key = getPropValue(item, properties);
                    hashmap.put((K) key, (V) item);
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
            return hashmap;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <K, V> List<V> MapToList(Map<K, V> map) {
        List<V> list = new ArrayList<V>();
        for (V v : map.values()) {
            list.add(v);
        }
        return list;
    }

    public static <K, V, T, L> Map<K, V> listToMap(List<L> list, String keyPropertieName) {
        return listToMap(list, keyPropertieName, HashMap.class);
    }

//	public static <K,V,T extends L,L> Map<K,V> listToMap(List<T> list, Class<L> prikeyClazz) {
//		return listToMap(list,prikeyClazz,HashMap.class);
//	}

//	public static <K,V,T extends L,L> Map<K,V> listToMap(List<T> list, Class<L> prikeyClazz, Class<T> mapclass){
//		try {
//			Map<K,V> hashmap = (Map<K,V>)mapclass.newInstance();
//			for (Iterator<L> iter = list.iterator(); iter.hasNext();) {
//				Object item = iter.next();
//				try {
//					Object key = getPropValue(item, prikeyClazz);
//					hashmap.put((K)key, (V)item);
//				} catch (Exception ex) {
//					throw new RuntimeException(ex.getMessage());
//				}
//			}
//			return hashmap;
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    private static String formatProperties(String name) {
        if (name.length() > 1) {
            if (Character.isLowerCase(name.charAt(0))) {// 小写
                if (Character.isLowerCase(name.charAt(1))) {// 小写
                    return String.valueOf(name.charAt(0)).toUpperCase()
                            + name.substring(1);
                } else {
                    return String.valueOf(name.charAt(0)).toLowerCase()
                            + name.substring(1);
                }
            } else {
                return name;
            }
        }
        return String.valueOf(name.charAt(0)).toUpperCase();
    }

    /**
     * 解析数据库中物品的数据
     *
     * @param obj
     * @param properties
     * @return
     * @throws Exception
     */
    public static <T> Object getPropValue(T obj, String properties) throws Exception {
        String methodname = "get" + formatProperties(properties);
        Method method = obj.getClass().getMethod(methodname);
        Object keypropvalue = method.invoke(obj);
        return keypropvalue;
    }

    public static <T, E extends T> T getPropValue(E obj, Class<T> prikeyClazz) throws Exception {
        T t = prikeyClazz.cast(obj);
        return t;
    }

    /**
     * 更新 map中 集合对象中的数据
     * <p/>
     * Map<Integer,Set<InstanceStageData>>
     * <p/>
     * 目标：如果Map中没有这个集合,直接加到Map中; 如果Map中有这个集合,更新这个集合数据
     */
    @SuppressWarnings("unchecked")
    public static <K, V, L> void addOrupdateMapGroupData(Map<K, V> map, Collection<L> collection, String property) throws Exception {
        if (collection == null) {
            return;
        }
        for (L newobj : collection) {
            Object key = getPropValue(newobj, property);
            Object obj = map.get(key);
            List<L> list;
            if (obj == null) {
                list = new ArrayList<L>();
                map.put((K) key, (V) list);
            } else {
                list = (List<L>) obj;
            }
            if (!list.contains(newobj)) {
                list.add(newobj);
            }
        }
    }

    public static void main(String[] args) throws Exception {
//		System.out.println(formatProperties("fId"));
//		System.out.println(formatProperties("FId"));
//		System.out.println(formatProperties("fid"));
    }
}
