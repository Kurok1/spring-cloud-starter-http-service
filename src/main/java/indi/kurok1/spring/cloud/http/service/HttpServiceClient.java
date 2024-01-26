package indi.kurok1.spring.cloud.http.service;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HttpServiceClient {

    String serviceId();

    String url();

    String path() default "";

    Class<?>[] configurations() default {};

}
