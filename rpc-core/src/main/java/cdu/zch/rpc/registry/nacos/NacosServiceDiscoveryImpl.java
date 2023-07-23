package cdu.zch.rpc.registry.nacos;

import cdu.zch.rpc.enums.LoadBalanceEnum;
import cdu.zch.rpc.enums.RpcErrorMessageEnum;
import cdu.zch.rpc.exception.RpcException;
import cdu.zch.rpc.extension.ExtensionLoader;
import cdu.zch.rpc.loadbalance.LoadBalance;
import cdu.zch.rpc.registry.ServiceDiscovery;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.utils.NacosUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Zch
 * @date 2023/7/23
 **/
@Slf4j
public class NacosServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public NacosServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(LoadBalanceEnum.LOADBALANCE.getName());
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(rpcRequest.getRpcServiceName());
            if(instances.size() == 0) {
                log.error("找不到对应的服务: " + rpcRequest.getInterfaceName());
                throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
            }
            return new InetSocketAddress(instances.get(0).getIp(), instances.get(0).getPort());
        } catch (NacosException e) {
            log.error("获取服务时错误！", e);
        }
        return null;
    }
}
