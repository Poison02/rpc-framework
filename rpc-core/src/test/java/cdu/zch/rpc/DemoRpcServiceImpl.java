package cdu.zch.rpc;

import cdu.zch.rpc.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@Slf4j
@RpcService(version = "version1", group = "test1")
public class DemoRpcServiceImpl implements DemoRpcService{
    @Override
    public String hello() {
        return "Hello!";
    }
}
