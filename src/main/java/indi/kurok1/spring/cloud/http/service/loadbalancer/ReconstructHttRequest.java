package indi.kurok1.spring.cloud.http.service.loadbalancer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;

import java.net.URI;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
class ReconstructHttRequest implements HttpRequest {

    private final HttpRequest delegate;
    private final URI reconstructUri;

    public ReconstructHttRequest(HttpRequest delegate, URI reconstructUri) {
        this.delegate = delegate;
        this.reconstructUri = reconstructUri;
    }

    @Override
    public HttpMethod getMethod() {
        return delegate.getMethod();
    }

    @Override
    public URI getURI() {
        return reconstructUri;
    }

    @Override
    public HttpHeaders getHeaders() {
        return delegate.getHeaders();
    }
}
