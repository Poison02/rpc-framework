package cdu.zch.rpc.loadbalance.loadbalancer;

import cdu.zch.rpc.loadbalance.AbstractLoadBalance;
import cdu.zch.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单的轮询策略
 * @author Zch
 * @date 2023/7/24
 **/
public class RoundRobinBalance extends AbstractLoadBalance {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        int size = serviceUrlList.size();
        int index = (atomicInteger.getAndIncrement() + size) % size;
        return serviceUrlList.get(index);
    }
}
