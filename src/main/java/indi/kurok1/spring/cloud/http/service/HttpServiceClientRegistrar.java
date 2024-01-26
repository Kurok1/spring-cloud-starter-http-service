package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.autoconfigure.ConditionalOnHttpInterfaceEnabled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
@ConditionalOnHttpInterfaceEnabled
class HttpServiceClientRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, EnvironmentAware, BeanFactoryAware, BeanClassLoaderAware {

    private final static Logger logger = LoggerFactory.getLogger(HttpServiceClientRegistrar.class);

    private ResourceLoader resourceLoader;
    private Environment environment;
    private ConfigurableBeanFactory beanFactory;
    private ClassLoader classLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerHttpServiceClient(importingClassMetadata, registry);
    }

    private void registerHttpServiceClient(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableHttpClients.class.getName());

        //get base packages
        String[] basePackages = (String[]) attrs.get("basePackages");
        if (basePackages == null || basePackages.length == 0) {
            logger.info("found @EnableHttpClients but not set basePackages, did you forget ?");
            return;
        }

        //if exclude client
        final Class<?>[] excludeClients = attrs == null ? null : (Class<?>[]) attrs.get("excludeClients");
        HashSet<String> excludeClientSet = new HashSet<>();
        if (excludeClients != null) {
            for (Class<?> client : excludeClients) {
                excludeClientSet.add(client.getName());
            }
        }

        ClassPathScanningCandidateComponentProvider scanner = getScanner(excludeClientSet);
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(HttpServiceClient.class));

        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition beanDefinition) {
                // verify annotated class is an interface
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(HttpServiceClient.class.getCanonicalName());

                String serviceId = getServiceId(attributes);
                String className = annotationMetadata.getClassName();
                Class<?> clientType = ClassUtils.resolveClassName(className, this.classLoader);;
                registerHttpServiceClientSpecification(registry, annotationMetadata.getClassName(), serviceId, attributes.get("configurations"));
                registerHttpServiceClientBean(registry, annotationMetadata, attributes, serviceId, clientType);
            }
        }

    }

    private void registerHttpServiceClientBean(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes,
                                    String serviceId, Class<?> clientType) {
        //fetch url
        String url = (String) attributes.get("url");
        //fetch path
        String path = (String) attributes.get("path");

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(HttpServiceClientFactoryBean.class);
        definition.addConstructorArgValue(serviceId);
        definition.addConstructorArgValue(url);
        definition.addConstructorArgValue(path);
        definition.addConstructorArgValue(clientType);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, clientType);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, clientType.getCanonicalName());
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

    }

    private void registerHttpServiceClientSpecification(BeanDefinitionRegistry registry, Object className, String serviceName,
                                             Object configurations) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(HttpServiceClientSpecification.class);
        builder.addConstructorArgValue(serviceName);
        builder.addConstructorArgValue(configurations);
        registry.registerBeanDefinition(className + "." + serviceName + "." + HttpServiceClientSpecification.class.getSimpleName(),
                builder.getBeanDefinition());
    }

    private String getServiceId(Map<String, Object> attributes) {
        final String serviceId = attributes.get("serviceId").toString();
        return resolveValue(serviceId);
    }

    protected ClassPathScanningCandidateComponentProvider getScanner(final Set<String> excludeClients) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false){
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };

        provider.setResourceLoader(this.resourceLoader);
        provider.setEnvironment(this.environment);
        provider.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            if (!classMetadata.isIndependent())
                return true;

            //class must is an interface
            if (!classMetadata.isInterface())
                return true;

            String className = metadataReader.getClassMetadata().getClassName();
            return excludeClients.contains(className);
        });

        return provider;
    }

    private String resolveValue(String value) {
        if (StringUtils.hasText(value)) {
            if (beanFactory == null) {
                return this.environment.resolvePlaceholders(value);
            }
            BeanExpressionResolver resolver = beanFactory.getBeanExpressionResolver();
            String resolved = beanFactory.resolveEmbeddedValue(value);
            if (resolver == null) {
                return resolved;
            }
            Object evaluateValue = resolver.evaluate(resolved, new BeanExpressionContext(beanFactory, null));
            if (evaluateValue != null) {
                return String.valueOf(evaluateValue);
            }
            return null;
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableBeanFactory)) {
            logger.debug("current bean factory is not a instance for ConfigurableBeanFactory");
            return;
        }

        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
