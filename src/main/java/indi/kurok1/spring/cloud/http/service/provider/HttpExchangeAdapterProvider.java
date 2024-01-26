package indi.kurok1.spring.cloud.http.service.provider;

import indi.kurok1.spring.cloud.http.service.HttpServiceClientContext;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface HttpExchangeAdapterProvider  {

    HttpExchangeAdapter create(String serviceId, String url, Class<?> clientType, HttpServiceClientContext context);

}
