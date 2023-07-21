package cdu.zch.rpc.serialize.kryo;

import cdu.zch.rpc.remoting.dto.RpcRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class KryoSerializerTest {

    @Test
    public void kryoSerializerTest() {
        RpcRequest target = RpcRequest.builder().methodName("hello")
                .parameters(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("cdu.zch.rpc.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("version1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] bytes = kryoSerializer.serialize(target);
        System.out.println(Arrays.toString(bytes));
        RpcRequest actual = kryoSerializer.deserialize(bytes, RpcRequest.class);
        System.out.println(actual);
        assertEquals(target.getGroup(), actual.getGroup());
        assertEquals(target.getVersion(), actual.getVersion());
        assertEquals(target.getRequestId(), actual.getRequestId());
    }

}
