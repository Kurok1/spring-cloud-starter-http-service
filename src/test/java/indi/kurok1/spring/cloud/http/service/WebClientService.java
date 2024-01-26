package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.configuration.UsingWebClientConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@HttpServiceClient(serviceId = "web-client-service", url = "http://web-client-service/",
        path = "/biz",
        configurations = UsingWebClientConfiguration.class)
public interface WebClientService {

    @GetExchange("/echo/{value}")
    Mono<String> echo(@PathVariable("value") String value);
}
