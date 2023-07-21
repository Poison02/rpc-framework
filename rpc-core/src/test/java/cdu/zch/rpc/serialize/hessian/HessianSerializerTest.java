package cdu.zch.rpc.serialize.hessian;

import cdu.zch.rpc.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class HessianSerializerTest {

    @Test
    public void hessianTest() {
        RpcRequest target = RpcRequest.builder().methodName("hello")
                .parameters(new Object[] {"sayhello", "hellolooloo"})
                .interfaceName("cdu.zch.rpc.HelloService")
                .paramTypes(new Class<?>[] {String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("version1")
                .build();
        HessianSerializer hessianSerializer = new HessianSerializer();
        byte[] bytes = hessianSerializer.serialize(target);
        System.out.println(Arrays.toString(bytes));
        RpcRequest actual = hessianSerializer.deserialize(bytes, RpcRequest.class);
        System.out.println(actual);
    }

}
