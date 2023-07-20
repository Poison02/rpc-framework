package cdu.zch.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC service注解，标记service实现类
 * @author Zch
 * @date 2023/7/20
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    String version() default "";

    String group() default "";

}
