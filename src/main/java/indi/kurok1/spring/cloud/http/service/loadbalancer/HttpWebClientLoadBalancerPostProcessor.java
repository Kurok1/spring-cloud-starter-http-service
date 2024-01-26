package indi.kurok1.spring.cloud.http.service.loadbalancer;

import org.springframework.beans.BeansException;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerWebClientBuilderBeanPostProcessor;
import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpWebClientLoadBalancerPostProcessor extends LoadBalancerWebClientBuilderBeanPostProcessor {

    public HttpWebClientLoadBalancerPostProcessor(DeferringLoadBalancerExchangeFilterFunction exchangeFilterFunction, ApplicationContext context) {
        super(exchangeFilterFunction, context);
    }
}
