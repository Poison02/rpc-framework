package cdu.zch.rpc.utils;

import java.util.Collection;

/**
 * 集合工具类
 * @author Zch
 * @date 2023/7/20
 **/
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

}
