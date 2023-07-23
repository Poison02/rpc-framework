package cdu.zch.rpc.config;

import cdu.zch.rpc.registry.zk.util.CuratorUtil;
import cdu.zch.rpc.remoting.netty.server.NettyServer;
import cdu.zch.rpc.utils.NacosUtil;
import cdu.zch.rpc.utils.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * When the server  is closed, do something such as unregister all services
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
public class CustomShutdownHook {

    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyServer.PORT);
                CuratorUtil.clearRegistry(CuratorUtil.getZkClient(), inetSocketAddress);

                NacosUtil.clearRegistry();
            } catch (UnknownHostException ignored) {

            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }

}
