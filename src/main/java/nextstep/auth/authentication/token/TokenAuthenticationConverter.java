package nextstep.auth.authentication.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UsernamePasswordAuthenticationToken;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {
    private final ObjectMapper objectMapper;

    public TokenAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        try {
            TokenRequest tokenRequest = objectMapper.readValue(request.getInputStream(), TokenRequest.class);
            String principal = tokenRequest.getEmail();
            String credentials = tokenRequest.getPassword();

            return new UsernamePasswordAuthenticationToken(principal, credentials);
        } catch (IOException e) {
            return null;
        }
    }

}
