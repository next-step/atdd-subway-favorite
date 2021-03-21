package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.ui.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private final ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        InputStream requestBody = getRequestBody(request);
        TokenRequest tokenRequest = getTokenRequest(requestBody);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    private TokenRequest getTokenRequest(InputStream requestBody) {
        TokenRequest tokenRequest;
        try {
            tokenRequest = objectMapper.readValue(requestBody, TokenRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return tokenRequest;
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestBody;
        try {
            requestBody =  request.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return requestBody;
    }
}
