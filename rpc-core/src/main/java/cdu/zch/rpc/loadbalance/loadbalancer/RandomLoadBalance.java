package cdu.zch.rpc.loadbalance.loadbalancer;

import cdu.zch.rpc.loadbalance.AbstractLoadBalance;
import cdu.zch.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * 随机策略
 * @author Zch
 * @date 2023/7/21
 **/
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceUrlList.get(random.nextInt(serviceUrlList.size()));
    }
}
