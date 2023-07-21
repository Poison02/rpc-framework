package cdu.zch.rpc.proxy;

import cdu.zch.rpc.config.RpcServiceConfig;
import cdu.zch.rpc.enums.RpcErrorMessageEnum;
import cdu.zch.rpc.enums.RpcRequestTransportEnum;
import cdu.zch.rpc.enums.RpcResponseCodeEnum;
import cdu.zch.rpc.exception.RpcException;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.remoting.dto.RpcResponse;
import cdu.zch.rpc.remoting.netty.RpcRequestTransport;
import cdu.zch.rpc.remoting.netty.client.NettyClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 动态代理类。
 * 动态代理对象调用方法时，实际上调用的是下面的invoke方法。
 * 正是因为有了动态代理，客户端调用远程方法就像调用本地方法一样（屏蔽了中间过程）
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";

    private final RpcRequestTransport rpcRequestTransport;

    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * 获得代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 当你使用代理对象调用方法时，实际上会调用这个方法。
     * 代理对象就是你通过getProxy方法得到的对象。
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method: [{}]", method.getName());
        // 构建请求对象
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        // 响应对象
        RpcResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof NettyClient) {
            // 通过CompletableFuture异步构建响应对象
            CompletableFuture<RpcResponse<Object>> completableFuture =
                    (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        System.out.println(rpcRequest);
        System.out.println(rpcResponse);
        this.check(rpcResponse, rpcRequest);

        return rpcResponse.getData();
    }

    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
