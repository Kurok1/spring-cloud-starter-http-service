package indi.kurok1.spring.cloud.http.service.provider;

import indi.kurok1.spring.cloud.http.service.HttpServiceClientContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpExchangeAdapter;

/**
 * implements {@link HttpExchangeAdapterProvider} for {@link org.springframework.web.reactive.function.client.WebClient}
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@ConditionalOnClass(WebClient.class)
public class WebClientAdapterProvider implements HttpExchangeAdapterProvider {

    @Override
    public HttpExchangeAdapter create(String serviceId, String url, Class<?> clientType, HttpServiceClientContext context) {
        WebClient.Builder builder = context.getInstance(serviceId, WebClient.Builder.class);
        if (builder == null)
            builder = WebClient.builder();

        apply(builder, serviceId, context);
        WebClient webClient = builder.baseUrl(url).build();
        return WebClientAdapter.create(webClient);
    }

    protected void apply(WebClient.Builder builder, String serviceId, HttpServiceClientContext context) {
        //spring boot
        ObjectProvider<WebClientCustomizer> clientCustomizers = context.getProvider(serviceId, WebClientCustomizer.class);
        clientCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
        //spring cloud
        ObjectProvider<org.springframework.cloud.client.loadbalancer.reactive.WebClientCustomizer> cloudClientCustomizers = context.getProvider(serviceId, org.springframework.cloud.client.loadbalancer.reactive.WebClientCustomizer.class);
        cloudClientCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
    }
}
