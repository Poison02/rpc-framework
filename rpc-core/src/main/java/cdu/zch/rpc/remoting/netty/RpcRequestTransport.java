package cdu.zch.rpc.remoting.netty;

import cdu.zch.rpc.extension.SPI;
import cdu.zch.rpc.remoting.dto.RpcRequest;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@SPI
public interface RpcRequestTransport {

    /**
     * send rpc request to server and get result
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RpcRequest rpcRequest);

}
