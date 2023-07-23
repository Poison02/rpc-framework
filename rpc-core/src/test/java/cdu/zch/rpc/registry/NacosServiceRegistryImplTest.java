package cdu.zch.rpc.registry;

import cdu.zch.rpc.DemoRpcService;
import cdu.zch.rpc.DemoRpcServiceImpl;
import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.registry.nacos.NacosServiceRegistryImpl;
import cdu.zch.rpc.registry.zk.ZkServiceDiscoveryImpl;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.utils.NacosUtil;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

/**
 * @author Zch
 * @date 2023/7/23
 **/
public class NacosServiceRegistryImplTest {

    @Test
    public void test() throws NacosException {
        NacosServiceRegistryImpl nacosServiceRegistry = new NacosServiceRegistryImpl();
        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);

        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test2").version("version2").service(demoRpcService).build();
        System.out.println(rpcServiceConfig);

        nacosServiceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), givenInetSocketAddress);

        RpcRequest rpcRequest = RpcRequest.builder()
//                .parameters(args)
                .interfaceName(rpcServiceConfig.getServiceName())
//                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();

        System.out.println(rpcRequest);
        List<Instance> instances = NacosUtil.getAllInstance(rpcRequest.getRpcServiceName());
        System.out.println(instances);
        System.out.println(new InetSocketAddress(instances.get(0).getIp(), instances.get(0).getPort()));

    }

}
