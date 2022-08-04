package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends InterceptorChainingFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected UserDetails getUserDetails(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        return User.of(principal, roles);
    }

    @Override
    protected void setAuthentication(UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
