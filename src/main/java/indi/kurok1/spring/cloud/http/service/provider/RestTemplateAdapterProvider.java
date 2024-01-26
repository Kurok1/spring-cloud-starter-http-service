package indi.kurok1.spring.cloud.http.service.provider;

import indi.kurok1.spring.cloud.http.service.HttpServiceClientContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * implements {@link HttpExchangeAdapterProvider} for {@link org.springframework.web.client.RestTemplate}
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class RestTemplateAdapterProvider implements HttpExchangeAdapterProvider {

    @Override
    public HttpExchangeAdapter create(String serviceId, String url, Class<?> clientType, HttpServiceClientContext context) {
        RestTemplate restTemplate = context.getInstance(serviceId, RestTemplate.class);
        customize(restTemplate, serviceId, context);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));
        return  RestTemplateAdapter.create(restTemplate);
    }

    protected void customize(RestTemplate restTemplate, String serviceId, HttpServiceClientContext context) {
        ObjectProvider<RestTemplateCustomizer> customizerObjectProvider = context.getProvider(serviceId, RestTemplateCustomizer.class);
        customizerObjectProvider.orderedStream().forEach(customizer -> customizer.customize(restTemplate));
    }
}
