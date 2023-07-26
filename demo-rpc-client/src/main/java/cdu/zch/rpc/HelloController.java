package cdu.zch.rpc;

import cdu.zch.rpc.annotation.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @author Zch
 * @date 2023/7/21
 */
@Component
public class HelloController {

    @RpcReference(version = "version1", group = "test1")
    private HelloService helloService;

    public void test() throws InterruptedException {
        String hello = this.helloService.hello(new Hello("111", "222"));
        System.out.println("调用结果是： " + hello);
    }
}
