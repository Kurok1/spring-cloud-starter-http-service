package indi.kurok1.spring.cloud.http.service;

import indi.kurok1.spring.cloud.http.service.provider.HttpExchangeAdapterProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpServiceClientFactoryBean
    implements FactoryBean<Object>, BeanFactoryAware {

    private final String serviceName;
    private String url;
    private final String path;
    private final Class<?> clientType;


    //required components
    private HttpServiceClientContext httpServiceClientContext;

    public HttpServiceClientFactoryBean(String serviceName, String url, String path, Class<?> clientType) {
        Assert.hasText(serviceName, "serviceName cannot be empty");
        Assert.hasText(url, "url cannot be empty");
        Assert.notNull(clientType, "clientType cannot be null");
        this.serviceName = serviceName;
        this.url = url;
        this.path = path;
        this.clientType = clientType;
    }

    @Override
    public Object getObject() throws Exception {
        return createClient(this.httpServiceClientContext);
    }

    private Object createClient(HttpServiceClientContext context) {
        HttpExchangeAdapterProvider provider = context.getInstance(serviceName, HttpExchangeAdapterProvider.class);

        HttpExchangeAdapter adapter = provider.create(serviceName, baseUrl(), clientType, context);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(clientType);
    }

    private String baseUrl() {
        try {
            new URL(url);
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException(url + " is malformed", e);
        }
        if (url.endsWith("/"))
            url = url.substring(0, url.length() - 1);
        if (path.startsWith("/"))
            return url + path;
        else return url + "/" + path;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.httpServiceClientContext = beanFactory.getBean(HttpServiceClientContext.class);
    }

    @Override
    public Class<?> getObjectType() {
        return clientType;
    }

}
