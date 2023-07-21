package cdu.zch.rpc.registry.zk;

import cdu.zch.rpc.enums.LoadBalanceEnum;
import cdu.zch.rpc.enums.RpcErrorMessageEnum;
import cdu.zch.rpc.exception.RpcException;
import cdu.zch.rpc.extension.ExtensionLoader;
import cdu.zch.rpc.loadbalance.LoadBalance;
import cdu.zch.rpc.registry.ServiceDiscovery;
import cdu.zch.rpc.registry.zk.util.CuratorUtil;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(LoadBalanceEnum.LOADBALANCE.getName());
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        List<String> serviceUrlList = CuratorUtil.getChildrenNodes(zkClient, rpcServiceName);
        // 如果注册列表为空，则抛出异常
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address : [{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
