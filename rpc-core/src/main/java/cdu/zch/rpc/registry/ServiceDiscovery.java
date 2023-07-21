package cdu.zch.rpc.registry;

import cdu.zch.rpc.extension.SPI;
import cdu.zch.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@SPI
public interface ServiceDiscovery {

    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);

}
