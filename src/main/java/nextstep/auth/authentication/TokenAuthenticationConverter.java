package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        TokenRequest tokenRequest = tokenRequest(request);
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    private TokenRequest tokenRequest(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(request.getInputStream(), TokenRequest.class);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
