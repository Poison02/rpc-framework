package cdu.zch.rpc.annotation;

import java.lang.annotation.*;

/**
 * RPC reference 注解，自动注入服务实现类
 * @author Zch
 * @date 2023/7/20
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    /**
     * 服务版本号
     */
    String version() default "";

    /**
     * 服务组
     */
    String group() default "";

}
