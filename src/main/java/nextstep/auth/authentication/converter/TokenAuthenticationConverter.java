package nextstep.auth.authentication.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.exception.TokenAuthenticationConvertException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        TokenRequest tokenRequest = null;
        try {
            tokenRequest = new ObjectMapper().readValue(request.getInputStream(), TokenRequest.class);
        } catch (IOException e) {
            throw new TokenAuthenticationConvertException();
        }
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
