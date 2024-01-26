package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.autoconfigure.HttpInterfaceDefaultConfiguration;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpServiceClientContext extends NamedContextFactory<HttpServiceClientSpecification> {


    private static final String propertySourceName = "spring.cloud.http.client";
    private static final String propertyName = "spring.cloud.http.client.name";
    private static final Class<HttpInterfaceDefaultConfiguration> defaultConfigType = HttpInterfaceDefaultConfiguration.class;

    public HttpServiceClientContext() {
        this(new HashMap<>());
    }

    public HttpServiceClientContext(Map<String, ApplicationContextInitializer<GenericApplicationContext>> initializerMap) {
        super(defaultConfigType, propertySourceName, propertyName, initializerMap);
    }

    public HttpServiceClientContext withApplicationContextInitializers(Map<String, Object> applicationContextInitializers) {
        Map<String, ApplicationContextInitializer<GenericApplicationContext>> convertedInitializers = new HashMap<>();
        applicationContextInitializers.keySet()
                .forEach(contextId -> convertedInitializers.put(contextId,
                        (ApplicationContextInitializer<GenericApplicationContext>) applicationContextInitializers
                                .get(contextId)));
        return new HttpServiceClientContext(convertedInitializers);
    }
}
