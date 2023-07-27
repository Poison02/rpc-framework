package cdu.zch.rpc.utils;

import cdu.zch.rpc.enums.RpcConfigEnum;
import cdu.zch.rpc.enums.RpcErrorMessageEnum;
import cdu.zch.rpc.exception.RpcException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Nacos连接工具类
 * @author Zch
 * @date 2023/7/23
 **/
@Slf4j
public class NacosUtil {

    private static final NamingService namingService;

    private static final Set<String> serviceNames = new HashSet<>();

    private static InetSocketAddress address;

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    static {
        namingService = getNacosNamingService();
    }

    public static NamingService getNacosNamingService() {
        try {
            // 检查用户是否设置了nacos地址
            Properties properties = PropertiesFileUtil.readPropertiesFile(RpcConfigEnum.RPC_CONFIG_PATH.getPropertyValue());
            String nacosAddress =
                    properties != null && properties.getProperty(RpcConfigEnum.NACOS_ADDRESS.getPropertyValue()) != null
                            ? properties.getProperty(RpcConfigEnum.NACOS_ADDRESS.getPropertyValue())
                            : SERVER_ADDR;
            if (nacosAddress == null) {
                return null;
            }
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (Exception e) {
            log.error("连接到Nacos时有错误发生！", e);
            throw new RpcException(RpcErrorMessageEnum.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(String serviceName, InetSocketAddress address) throws NacosException {
        namingService.registerInstance(serviceName, address.getHostName(), address.getPort());
        NacosUtil.address = address;
        serviceNames.add(serviceName);
    }

    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    public static void clearRegistry() {
        if(!serviceNames.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while(iterator.hasNext()) {
                String serviceName = iterator.next();
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }
}
