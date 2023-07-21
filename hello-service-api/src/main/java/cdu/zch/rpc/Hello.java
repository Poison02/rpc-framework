package cdu.zch.rpc;

import lombok.*;

import java.io.Serializable;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Hello implements Serializable {

    private String message;
    private String description;

}
