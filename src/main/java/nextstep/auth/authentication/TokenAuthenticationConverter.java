package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.token.TokenRequest;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    private final ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();
        return new AuthenticationToken(principal, credentials);
    }
}
