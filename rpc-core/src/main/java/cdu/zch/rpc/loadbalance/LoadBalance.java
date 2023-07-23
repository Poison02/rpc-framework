package cdu.zch.rpc.loadbalance;

import cdu.zch.rpc.extension.SPI;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡策略接口
 * @author Zch
 * @date 2023/7/21
 **/
@SPI
public interface LoadBalance {

    /**
     * 从已注册的服务地址中选择一个
     * @param serviceUrlList 服务列表
     * @param rpcRequest 请求
     * @return 服务地址
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);

}
