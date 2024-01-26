package indi.kurok1.spring.cloud.http.service.loadbalancer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link HttpStatus.SERVICE_UNAVAILABLE} response
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0
 */
class ServiceUnavailableResponse implements ClientHttpResponse {

    public final static ServiceUnavailableResponse INSTANCE = new ServiceUnavailableResponse();

    private ServiceUnavailableResponse() {

    }
    @Override
    public HttpStatusCode getStatusCode() throws IOException {
        return HttpStatusCode.valueOf(503);
    }

    @Override
    public String getStatusText() throws IOException {
        return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getBody() throws IOException {
        return null;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
