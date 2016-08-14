package mmo.util;

import java.util.Collection;

/**
 * @author Jin Shuai
 */
public class CollectionUtil {
    public static boolean isNull(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNull(Collection collection) {
        return !isNull(collection);
    }
}
