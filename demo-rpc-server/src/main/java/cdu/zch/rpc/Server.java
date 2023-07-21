package cdu.zch.rpc;

import cdu.zch.rpc.annotation.RpcScan;
import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.remoting.netty.server.NettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@RpcScan(basePackage = {"cdu.zch.rpc"})
public class Server {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Server.class);
        NettyServer nettyServer = applicationContext.getBean("nettyServer", NettyServer.class);

        HelloService helloService = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("test1")
                .version("version1")
                .service(helloService)
                .build();
        nettyServer.registerService(rpcServiceConfig);
        nettyServer.start();
    }

}
