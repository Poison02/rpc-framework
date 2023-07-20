package cdu.zch.rpc.remoting.dto;

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
public class RpcMessage {

    private byte messageType;

    private byte codec;

    private byte compress;

    private int requestId;

    private Object data;

}
