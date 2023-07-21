package cdu.zch.rpc.loadbalance.loadbalancer;

import cdu.zch.rpc.DemoRpcService;
import cdu.zch.rpc.DemoRpcServiceImpl;
import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.enums.LoadBalanceEnum;
import cdu.zch.rpc.extension.ExtensionLoader;
import cdu.zch.rpc.loadbalance.LoadBalance;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class ConsistentHashLoadBalanceTest {

    @Test
    void consistentHashLoadBalanceTest() {
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(LoadBalanceEnum.LOADBALANCE.getName());
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:9997", "127.0.0.1:9998", "127.0.0.1:9999"));

        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test2").version("version2").service(demoRpcService).build();

        RpcRequest rpcRequest = RpcRequest.builder()
                .parameters(demoRpcService.getClass().getTypeParameters())
                .interfaceName(rpcServiceConfig.getServiceName())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        String userServiceAddress = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        System.out.println(userServiceAddress);
        assertEquals("127.0.0.1:9998", userServiceAddress);
    }

}
