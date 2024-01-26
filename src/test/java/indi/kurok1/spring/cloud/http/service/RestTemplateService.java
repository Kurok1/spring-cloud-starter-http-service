package indi.kurok1.spring.cloud.http.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@HttpServiceClient(serviceId = "rest-template-service", url = "http://rest-template-service/", path = "/biz")
public interface RestTemplateService {

    @GetExchange("/echo/{value}")
    String echo(@PathVariable("value") String value);

}
