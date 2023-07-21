package cdu.zch.rpc;

import cdu.zch.rpc.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author shuang.kou
 * @createTime 2020年05月10日 07:25:00
 */
@RpcScan(basePackage = {"cdu.zch.rpc"})
public class NettyClientMain {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
