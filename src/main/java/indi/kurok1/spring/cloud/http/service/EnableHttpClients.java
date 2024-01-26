package indi.kurok1.spring.cloud.http.service;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(HttpServiceClientRegistrar.class)
public @interface EnableHttpClients {

    String[] basePackages();

    Class<?>[] excludeClients() default {};

}
