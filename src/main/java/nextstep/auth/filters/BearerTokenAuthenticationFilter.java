package nextstep.auth.filters;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.filters.provider.AuthenticationProvider;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AuthenticationSavingFilter<String> {
    public BearerTokenAuthenticationFilter(AuthenticationProvider<String> authenticationProvider) {
        super(authenticationProvider);
    }

    @Override
    protected String convert(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }
}
