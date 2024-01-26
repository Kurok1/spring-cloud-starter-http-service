package indi.kurok1.spring.cloud.http.service.configuration;

import indi.kurok1.spring.cloud.http.service.provider.HttpExchangeAdapterProvider;
import indi.kurok1.spring.cloud.http.service.provider.RestClientHttpExchangeAdapterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
public class UsingRestClientConfiguration {
    @Bean
    public RestClient.Builder builder() {
        return RestClient.builder();
    }

    @Bean
    public HttpExchangeAdapterProvider restClientAdapterProvider() {
        return new RestClientHttpExchangeAdapterProvider();
    }
}
