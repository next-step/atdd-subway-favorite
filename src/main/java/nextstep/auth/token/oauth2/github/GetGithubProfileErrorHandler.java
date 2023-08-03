package nextstep.auth.token.oauth2.github;

import nextstep.auth.AuthenticationException;
import nextstep.auth.ForbiddenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.IOException;
import java.nio.charset.Charset;

public class GetGithubProfileErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);

        switch (statusCode) {
            case UNAUTHORIZED:
                throw new AuthenticationException("auth.0001");
            case FORBIDDEN:
                throw new ForbiddenException("auth.0002");
            default:
                throw new UnknownHttpStatusCodeException("unknownStatusCode", statusCode.value(), statusText, headers, body, charset);
        }
    }
}
