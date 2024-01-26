package indi.kurok1.spring.cloud.http.service.loadbalancer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;

/**
 * {@link org.springframework.web.client.RestClient} or {@link org.springframework.web.client.RestTemplate} implementation that uses {@link org.springframework.cloud.client.loadbalancer.LoadBalancerClient} to select a ServiceInstance to use while resolving the request host.
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HttpServiceClientLoadBalancerInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceClientLoadBalancerInterceptor.class);

    private final LoadBalancerClient loadBalancerClient;

    private final LoadBalancerClientFactory loadBalancerClientFactory;

    public HttpServiceClientLoadBalancerInterceptor(LoadBalancerClient loadBalancerClient, LoadBalancerClientFactory loadBalancerClientFactory) {
        this.loadBalancerClient = loadBalancerClient;
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        final URI uri = request.getURI();
        String serviceId = uri.getHost();
        Assert.state(serviceId != null, "Request URI does not contain a valid hostname: " + uri);
        ServiceInstance serviceInstance = this.loadBalancerClient.choose(serviceId);
        if (serviceInstance == null) {//service unavailable
            if (logger.isWarnEnabled()) {
                logger.warn("Load balancer does not contain an instance for the service " + serviceId);
            }
            return ServiceUnavailableResponse.INSTANCE;
        }
        //replace uri
        URI reconstructedUri = loadBalancerClient.reconstructURI(serviceInstance, uri);
        //continue
        return execution.execute(new ReconstructHttRequest(request, reconstructedUri), body);
    }
}
