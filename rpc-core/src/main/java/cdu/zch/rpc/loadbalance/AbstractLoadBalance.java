package cdu.zch.rpc.loadbalance;

import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.utils.CollectionUtil;

import java.util.List;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public abstract class AbstractLoadBalance implements LoadBalance{

    @Override
    public String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            return null;
        }
        if (serviceUrlList.size() == 1) {
            return serviceUrlList.get(0);
        }
        return doSelect(serviceUrlList, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceUrlList, RpcRequest rpcRequest);
}
