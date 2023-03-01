package nextstep.member.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TokenAuthInterceptor implements ClientHttpRequestInterceptor {

    private final String accessToken;

    public TokenAuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders()
            .set(HttpHeaders.AUTHORIZATION, "token " + accessToken);
        return execution.execute(request, body);
    }
}
