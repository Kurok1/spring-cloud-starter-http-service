package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.configuration.UsingRestClientConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@HttpServiceClient(serviceId = "rest-client-service", url = "http://rest-client-service/",
        path = "/biz",
        configurations = UsingRestClientConfiguration .class)
public interface RestClientService {

    @GetExchange("/echo/{value}")
    String echo(@PathVariable("value") String value);
}
