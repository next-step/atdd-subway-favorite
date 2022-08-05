package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

public class BearerFilter implements AuthorizationStrategy {
    private final JwtTokenProvider provider;
    private final LoginService loginService;

    public BearerFilter(JwtTokenProvider provider, LoginService loginService) {
        this.provider = provider;
        this.loginService = loginService;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    @Override
    public Authentication getAuthentication(String token) {
        return new Authentication(provider.getPrincipal(token), provider.getRoles(token));
    }


    @Override
    public Authentication extractAuthentication(String token) {
        if (!validToken(token)) {
            throw new AuthenticationException();
        }

        Authentication authentication = getAuthentication(token);

        if (!validUser(authentication)) {
            throw new AuthenticationException();
        }

        return authentication;
    }

    public boolean validToken(String token) {
        return provider.validateToken(token);
    }

    public boolean validUser(Authentication authentication) {
        return loginService.isUserExist((String) authentication.getPrincipal());
    }
}
