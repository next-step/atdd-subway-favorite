package nextstep.member.auth.interceptor;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class TokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {

    private final String accessToken;

    public TokenAuthenticationInterceptor(final String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        request.getHeaders().set(HttpHeaders.AUTHORIZATION, "token " + accessToken);
        return execution.execute(request, body);
    }
}
