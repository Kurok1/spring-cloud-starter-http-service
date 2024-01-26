package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.autoconfigure.HttpServiceClientAutoConfiguration;
import indi.kurok1.spring.cloud.http.service.loadbalancer.LoadBalancerHttpServiceClientAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerBeanPostProcessorAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */

@ExtendWith(SpringExtension.class)
@TestPropertySource(
        properties = {
                "spring.application.name=test",
                "spring.cloud.service-registry.auto-registration.enabled=true",
                "spring.cloud.discovery.client.simple.instances.web-client-service[0].host=127.0.0.1",
                "spring.cloud.discovery.client.simple.instances.web-client-service[0].port=8080",
                "spring.cloud.discovery.client.simple.instances.rest-client-service[0].host=127.0.0.1",
                "spring.cloud.discovery.client.simple.instances.rest-client-service[0].port=8080",
                "spring.cloud.discovery.client.simple.instances.rest-template-service[0].host=127.0.0.1",
                "spring.cloud.discovery.client.simple.instances.rest-template-service[0].port=8080"
        }
)
@EnableHttpClients(basePackages = "indi.kurok1.spring.cloud.http.service")
@EnableAutoConfiguration
public class HttpServiceTests {

    @Autowired
    private RestTemplateService restTemplateService;
    @Autowired
    private RestClientService restClientService;
    @Autowired
    private WebClientService webClientService;

    private final String value = "hello";

    @Test
    public void testWebClient() {
        String result = webClientService.echo(value).block();
        Assertions.assertEquals(result, value);
    }

    @Test
    public void testRestClient() {
        String result = restClientService.echo(value);
        Assertions.assertEquals(result, value);
    }

    @Test
    public void testRestTemplate() {
        String result = restTemplateService.echo(value);
        Assertions.assertEquals(result, value);
    }

}
