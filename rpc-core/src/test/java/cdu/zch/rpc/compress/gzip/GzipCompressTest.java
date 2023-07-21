package cdu.zch.rpc.compress.gzip;

import cdu.zch.rpc.compress.Compress;
import cdu.zch.rpc.remoting.dto.RpcRequest;
import cdu.zch.rpc.serialize.kryo.KryoSerializer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class GzipCompressTest {

    @Test
    void gzipCompressTest() {
        Compress gzipCompress = new GzipCompress();
        RpcRequest rpcRequest = RpcRequest.builder().methodName("hello")
                .parameters(new Object[]{"sayhelooloo", "sayhelooloosayhelooloo"})
                .interfaceName("cdu.zch.rpc.HelloService")
                .paramTypes(new Class<?>[]{String.class, String.class})
                .requestId(UUID.randomUUID().toString())
                .group("group1")
                .version("version1")
                .build();
        KryoSerializer kryoSerializer = new KryoSerializer();
        byte[] rpcRequestBytes = kryoSerializer.serialize(rpcRequest);
        byte[] compressRpcRequestBytes = gzipCompress.compress(rpcRequestBytes);
        System.out.println(Arrays.toString(compressRpcRequestBytes));
        byte[] decompressRpcRequestBytes = gzipCompress.decompress(compressRpcRequestBytes);
        assertEquals(rpcRequestBytes.length, decompressRpcRequestBytes.length);
    }

}
