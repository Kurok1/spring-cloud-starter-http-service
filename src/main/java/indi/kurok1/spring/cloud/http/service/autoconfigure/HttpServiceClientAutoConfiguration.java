package indi.kurok1.spring.cloud.http.service.autoconfigure;

import indi.kurok1.spring.cloud.http.service.HttpServiceClientContext;
import indi.kurok1.spring.cloud.http.service.HttpServiceClientSpecification;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnHttpInterfaceEnabled
public class HttpServiceClientAutoConfiguration {


    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpServiceClientContext HttpServiceClientContext(ObjectProvider<HttpServiceClientSpecification> specifications){
        HttpServiceClientContext context = new HttpServiceClientContext();
        context.setConfigurations(specifications.stream().toList());
        return context;
    }


}
