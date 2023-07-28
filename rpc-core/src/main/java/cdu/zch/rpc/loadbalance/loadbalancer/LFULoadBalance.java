package cdu.zch.rpc.loadbalance.loadbalancer;

import cdu.zch.rpc.loadbalance.AbstractLoadBalance;
import cdu.zch.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最近最少使用算法
 * @author Zch
 * @date 2023/7/28
 **/
public class LFULoadBalance extends AbstractLoadBalance {

    // Map用于存储每个服务URL的访问频率
    private final Map<String, AtomicInteger> serviceFrequency = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        return selectServiceUrl(serviceUrlList, rpcRequest);
    }

    private String selectServiceUrl(List<String> serviceUrlList, RpcRequest rpcRequest) {
        if (serviceUrlList.isEmpty()) {
            return null;
        }

        AtomicInteger minFrequency = null;
        String selectedServiceUrl = null;

        for (String serviceUrl : serviceUrlList) {
            AtomicInteger frequency = serviceFrequency.computeIfAbsent(serviceUrl, k -> new AtomicInteger(0));

            if (minFrequency == null || frequency.get() < minFrequency.get()) {
                minFrequency = frequency;
                selectedServiceUrl = serviceUrl;
            }
        }

        if (selectedServiceUrl != null) {
            // 使用compareAndSet进行原子递增
            int newFrequency = minFrequency.incrementAndGet();
            // 原子性地更新Map中的频率计数
            serviceFrequency.put(selectedServiceUrl, new AtomicInteger(newFrequency));
        }

        return selectedServiceUrl;
    }
}
