package indi.kurok1.spring.cloud.http.service;

import org.springframework.context.support.GenericApplicationContext;

/**
 * allow to customize HttpClientService's ApplicationContext
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface HttpServiceClientContextCustomizer {


    void customize(String serviceId, GenericApplicationContext context);

}
