package indi.kurok1.spring.cloud.http.service.loadbalancer;

import indi.kurok1.spring.cloud.http.service.autoconfigure.HttpServiceClientAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.reactive.WebClientCustomizer;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@ConditionalOnBean({ LoadBalancerClient.class, LoadBalancerClientFactory.class })
@AutoConfigureBefore(HttpServiceClientAutoConfiguration.class)
@AutoConfigureAfter({ BlockingLoadBalancerClientAutoConfiguration.class, LoadBalancerAutoConfiguration.class, LoadBalancerBeanPostProcessorAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
public class LoadBalancerHttpServiceClientAutoConfiguration {

    @ConditionalOnBean(DeferringLoadBalancerExchangeFilterFunction.class)
    @Bean
    public WebClientCustomizer loadBalancedWebClientCustomizer(DeferringLoadBalancerExchangeFilterFunction function) {
        return builder -> {
            builder.filter(function);
        };
    }

    @Bean
    public ClientLoadBalancerInterceptor clientLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient, LoadBalancerClientFactory loadBalancerClientFactory) {
        return new ClientLoadBalancerInterceptor(loadBalancerClient, loadBalancerClientFactory);
    }

}
