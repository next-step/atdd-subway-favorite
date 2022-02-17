package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthTokenConverter implements AuthenticationConverter {
    private ObjectMapper objectMapper;

    public TokenAuthTokenConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        TokenRequest tokenRequest;
        try {
            tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
        } catch (IOException e) {
            throw new AuthenticationException();
        }

        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
