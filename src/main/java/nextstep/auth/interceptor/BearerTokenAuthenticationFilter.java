package nextstep.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationChainHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public String extractCredentials(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    @Override
    public UserDetails getUserDetails(String authCredentials) {
        return userDetailsService.loadUserByUsername(jwtTokenProvider.getPrincipal(authCredentials));
    }

    @Override
    public Authentication createAuthentication(UserDetails userDetails) {
        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }
}
