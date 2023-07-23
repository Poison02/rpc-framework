package cdu.zch.rpc.registry.nacos;

import cdu.zch.rpc.enums.RpcErrorMessageEnum;
import cdu.zch.rpc.exception.RpcException;
import cdu.zch.rpc.registry.ServiceRegistry;
import cdu.zch.rpc.utils.NacosUtil;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Zch
 * @date 2023/7/23
 **/
@Slf4j
public class NacosServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(rpcServiceName, inetSocketAddress);
        } catch (NacosException e) {
            log.error("注册服务错误！");
            throw new RpcException(RpcErrorMessageEnum.REGISTER_SERVICE_FAILED);
        }
    }
}
