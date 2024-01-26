package indi.kurok1.spring.cloud.http.service;

import org.springframework.cloud.context.named.NamedContextFactory;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public record HttpServiceClientSpecification(String serviceName, Class<?>[] configurations) implements NamedContextFactory.Specification {

    @Override
    public String getName() {
        return serviceName;
    }

    @Override
    public Class<?>[] getConfiguration() {
        return configurations;
    }
}
