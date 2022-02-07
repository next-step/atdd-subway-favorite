package nextstep.auth.authentication.converter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import nextstep.auth.AuthConfig;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.token.TokenRequest;
import nextstep.common.util.ServletUtils;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter {
    @Override
    public boolean matchRequestUri(String url) {
        return AuthConfig.TOKEN_LOGIN_REQUEST_URI.equals(url);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        TokenRequest tokenRequest = ServletUtils.readJson(request, TokenRequest.class);
        return new AuthenticationToken(tokenRequest.getEmail(), tokenRequest.getPassword());
    }
}
