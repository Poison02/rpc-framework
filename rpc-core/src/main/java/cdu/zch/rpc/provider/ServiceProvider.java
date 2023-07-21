package cdu.zch.rpc.provider;

import cdu.zch.rpc.config.RpcServiceConfig;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public interface ServiceProvider {

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * @param rpcServiceConfig rpc service related attributes
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
