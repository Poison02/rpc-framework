package cdu.zch.rpc.registry;

import cdu.zch.rpc.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@SPI
public interface ServiceRegistry {

    /**
     * register service
     * @param rpcServiceName     rpc service name
     * @param inetSocketAddress  service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
