package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter{
    private final ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
