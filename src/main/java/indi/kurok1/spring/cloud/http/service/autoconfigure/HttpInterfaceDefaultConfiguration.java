package indi.kurok1.spring.cloud.http.service.autoconfigure;

import indi.kurok1.spring.cloud.http.service.provider.HttpExchangeAdapterProvider;
import indi.kurok1.spring.cloud.http.service.provider.RestTemplateAdapterProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * the default configuration for HttpInterface
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnHttpInterfaceEnabled
public class HttpInterfaceDefaultConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public HttpExchangeAdapterProvider httpExchangeAdapterProvider() {
        return new RestTemplateAdapterProvider();
    }

}
