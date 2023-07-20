package cdu.zch.rpc.config;

import lombok.*;

/**
 * @author Zch
 * @date 2023/7/20
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RpcServiceConfig {

    private String version = "";

    private String group = "";

    private Object service;

    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

}
