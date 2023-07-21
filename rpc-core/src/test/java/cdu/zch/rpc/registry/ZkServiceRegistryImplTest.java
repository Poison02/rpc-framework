package cdu.zch.rpc.registry;

import cdu.zch.rpc.DemoRpcService;
import cdu.zch.rpc.DemoRpcServiceImpl;
import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.registry.zk.ZkServiceDiscoveryImpl;
import cdu.zch.rpc.registry.zk.ZkServiceRegistryImpl;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class ZkServiceRegistryImplTest {

    @Test
    public void test() {
        ServiceRegistry zkServiceRegistry = new ZkServiceRegistryImpl();

        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);

        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test2").version("version2").service(demoRpcService).build();
        zkServiceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), givenInetSocketAddress);

        ServiceDiscovery zkServiceDiscovery = new ZkServiceDiscoveryImpl();

        RpcRequest rpcRequest = RpcRequest.builder()
//                .parameters(args)
                .interfaceName(rpcServiceConfig.getServiceName())
//                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        System.out.println(rpcRequest);
        InetSocketAddress acquiredInetSocketAddress = zkServiceDiscovery.lookupService(rpcRequest);
        System.out.println(acquiredInetSocketAddress);
        assertEquals(givenInetSocketAddress.toString(), acquiredInetSocketAddress.toString());
    }

}
