package cdu.zch.rpc.utils;

/**
 * @author Zch
 * @date 2023/7/20
 **/
public class RuntimeUtil {

    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }

}
