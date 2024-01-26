package indi.kurok1.spring.cloud.http.service.loadbalancer;

import org.springframework.cloud.client.loadbalancer.LoadBalancerRestClientBuilderBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpRestClientLoadBalancerPostProcessor extends LoadBalancerRestClientBuilderBeanPostProcessor {

    public HttpRestClientLoadBalancerPostProcessor(ClientHttpRequestInterceptor loadBalancerInterceptor, ApplicationContext context) {
        super(loadBalancerInterceptor, context);
    }
}
