package cdu.zch.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/20
 **/
@AllArgsConstructor
@Getter
public enum ServiceRegistryEnum {

    ZK("zk");

    private final String name;

}
