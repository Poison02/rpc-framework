package cdu.zch.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/20
 **/
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address"),
    NACOS_ADDRESS("rpc.nacos.address");

    private final String propertyValue;

}
