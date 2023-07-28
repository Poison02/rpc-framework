package cdu.zch.rpc.loadbalance.loadbalancer;

import cdu.zch.rpc.loadbalance.AbstractLoadBalance;
import cdu.zch.rpc.remoting.dto.RpcRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最久未被使用算法
 * @author Zch
 * @date 2023/7/28
 **/
public class LRULoadBalance extends AbstractLoadBalance {
    private final Map<String, AtomicInteger> serviceFrequency = new ConcurrentHashMap<>();
    private final Map<String, Long> serviceAccessOrder = new LinkedHashMap<>();

    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        if (serviceUrlList.isEmpty()) {
            return null;
        }

        String leastRecentlyUsedServiceUrl = getLeastRecentlyUsedServiceUrl();
        AtomicInteger frequency = serviceFrequency.computeIfAbsent(leastRecentlyUsedServiceUrl, k -> new AtomicInteger(0));
        frequency.incrementAndGet();

        return leastRecentlyUsedServiceUrl;
    }

    private String getLeastRecentlyUsedServiceUrl() {
        // 更新最久未被使用的服务URL的访问时间为当前时间
        String leastRecentlyUsedServiceUrl = serviceAccessOrder.entrySet().iterator().next().getKey();
        serviceAccessOrder.put(leastRecentlyUsedServiceUrl, System.nanoTime());

        return leastRecentlyUsedServiceUrl;
    }
}
