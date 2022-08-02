package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AuthenticationChainHandler {
    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected String extractCredentials(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    @Override
    protected UserDetails getUserDetails(String authCredentials) {
        return userDetailsService.loadUserByUsername(jwtTokenProvider.getPrincipal(authCredentials));
    }

    @Override
    protected Authentication createAuthentication(UserDetails userDetails) {
        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }
}
