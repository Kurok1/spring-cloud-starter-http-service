package indi.kurok1.spring.cloud.http.service.configuration;

import indi.kurok1.spring.cloud.http.service.provider.HttpExchangeAdapterProvider;
import indi.kurok1.spring.cloud.http.service.provider.WebClientAdapterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class UsingWebClientConfiguration {

    @Bean
    public WebClient.Builder builder() {
        return WebClient.builder();
    }

    @Bean
    public HttpExchangeAdapterProvider webClientAdapterProvider() {
        return new WebClientAdapterProvider();
    }

}
