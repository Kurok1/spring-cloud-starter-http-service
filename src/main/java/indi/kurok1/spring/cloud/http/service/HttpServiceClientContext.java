package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.autoconfigure.HttpInterfaceDefaultConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpServiceClientContext extends NamedContextFactory<HttpServiceClientSpecification> {


    private static final String propertySourceName = "spring.cloud.http.client";
    private static final String propertyName = "spring.cloud.http.client.name";
    private static final Class<HttpInterfaceDefaultConfiguration> defaultConfigType = HttpInterfaceDefaultConfiguration.class;

    private final List<HttpServiceClientContextCustomizer> customizers;

    public HttpServiceClientContext(ObjectProvider<HttpServiceClientContextCustomizer> contextCustomizers) {
        this(contextCustomizers, new HashMap<>());
    }

    public HttpServiceClientContext(ObjectProvider<HttpServiceClientContextCustomizer> contextCustomizers, Map<String, ApplicationContextInitializer<GenericApplicationContext>> initializerMap) {
        super(defaultConfigType, propertySourceName, propertyName, initializerMap);
        this.customizers = contextCustomizers.orderedStream().toList();
    }

    @Override
    public GenericApplicationContext buildContext(String name) {
        GenericApplicationContext context = super.buildContext(name);

        if (!CollectionUtils.isEmpty(this.customizers)) {
            customizers.forEach(customizer -> customizer.customize(name, context));
        }

        return context;
    }
}
