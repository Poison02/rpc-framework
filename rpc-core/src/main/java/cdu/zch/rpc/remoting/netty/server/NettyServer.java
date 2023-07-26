package cdu.zch.rpc.remoting.netty.server;

import cdu.zch.rpc.config.CustomShutdownHook;
import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.factory.SingletonFactory;
import cdu.zch.rpc.provider.ServiceProvider;
import cdu.zch.rpc.provider.impl.NacosServiceProviderImpl;
import cdu.zch.rpc.provider.impl.ZkServiceProviderImpl;
import cdu.zch.rpc.registry.nacos.NacosServiceRegistryImpl;
import cdu.zch.rpc.remoting.dto.RpcMessage;
import cdu.zch.rpc.remoting.netty.codec.RpcMessageDecoder;
import cdu.zch.rpc.remoting.netty.codec.RpcMessageEncoder;
import cdu.zch.rpc.utils.RuntimeUtil;
import cdu.zch.rpc.utils.threadpool.ThreadPoolFactoryUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
@Component
public class NettyServer {

    public static final int PORT = 9998;
    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    // private final ServiceProvider serviceProvider = SingletonFactory.getInstance(NacosServiceProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
        /*NacosServiceRegistryImpl nacosServiceRegistry = new NacosServiceRegistryImpl();
        nacosServiceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress("127.0.0.1", 9998));*/
    }

    @SneakyThrows
    public void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll();
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建用于执行事件处理任务的线程池，cpu核心的两倍，不是守护线程，指定线程池名称
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启Nagle算法，该算法的作用是尽可能的发送大数据快，减少网络传输，在这里设置为true表示禁用
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启TCP底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 30秒内没有收到客户端请求的话就关闭连接
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            p.addLast(new RpcMessageEncoder());
                            p.addLast(new RpcMessageDecoder());
                            p.addLast(serviceHandlerGroup, new RpcServerHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(host, PORT).sync();
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("occur exception when start server!", e);
        } finally {
            log.error("shutdown bossGroup and workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        }

    }

}
