package cdu.zch.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@AllArgsConstructor
@Getter
public enum LoadBalanceEnum {

    LOADBALANCE("loadBalance");

    private final String name;

}
