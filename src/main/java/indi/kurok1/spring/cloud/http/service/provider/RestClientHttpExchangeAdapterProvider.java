package indi.kurok1.spring.cloud.http.service.provider;

import indi.kurok1.spring.cloud.http.service.HttpServiceClientContext;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.observation.ClientRequestObservationConvention;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.util.UriBuilderFactory;

import java.util.List;

/**
 * implements {@link HttpExchangeAdapterProvider} for {@link org.springframework.web.client.RestClient}
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@ConditionalOnClass(RestClient.class)
public class RestClientHttpExchangeAdapterProvider implements HttpExchangeAdapterProvider {

    @Override
    public HttpExchangeAdapter create(String serviceId, String url, Class<?> clientType, HttpServiceClientContext context) {
        RestClient.Builder builder = context.getInstance(serviceId, RestClient.Builder.class);
        if (builder == null)
            builder = RestClient.builder();

        apply(builder, serviceId, context);
        builder.baseUrl(url);
        return RestClientAdapter.create(builder.build());
    }

    protected void apply(RestClient.Builder builder, String serviceId, HttpServiceClientContext context) {
        UriBuilderFactory uriBuilderFactory = context.getInstance(serviceId, UriBuilderFactory.class);
        if (uriBuilderFactory != null)
            builder.uriBuilderFactory(uriBuilderFactory);

        ResponseErrorHandler responseErrorHandler = context.getInstance(serviceId, ResponseErrorHandler.class);
        if (responseErrorHandler != null)
            builder.defaultStatusHandler(responseErrorHandler);

        ObjectProvider<ClientHttpRequestInterceptor> requestInterceptorProvider = context.getProvider(serviceId, ClientHttpRequestInterceptor.class);
        requestInterceptorProvider.orderedStream().forEach(i -> {
            if (!(i instanceof LoadBalancerInterceptor)) {
                builder.requestInterceptor(i);
            }
        });

        ClientHttpRequestInitializer requestInitializer = context.getInstance(serviceId, ClientHttpRequestInitializer.class);
        if (requestInitializer != null)
            builder.requestInitializer(requestInitializer);

        ClientHttpRequestFactory requestFactory = context.getInstance(serviceId, ClientHttpRequestFactory.class);
        if (requestFactory != null)
            builder.requestFactory(requestFactory);

        ObjectProvider<HttpMessageConverters> converters = context.getProvider(serviceId, HttpMessageConverters.class);
        converters.ifAvailable(c -> {
            List<HttpMessageConverter<?>> list = c.getConverters();
            if (!CollectionUtils.isEmpty(list))
                builder.messageConverters(configurer -> configurer.addAll(list));
        });


        ObservationRegistry registry = context.getInstance(serviceId, ObservationRegistry.class);
        if (registry != null)
            builder.observationRegistry(registry);

        ClientRequestObservationConvention convention = context.getInstance(serviceId, ClientRequestObservationConvention.class);
        if (convention != null)
            builder.observationConvention(convention);

        ObjectProvider<RestClientCustomizer> clientCustomizer = context.getProvider(serviceId, RestClientCustomizer.class);
        clientCustomizer.stream().forEach(customizer -> customizer.customize(builder));
    }
}
