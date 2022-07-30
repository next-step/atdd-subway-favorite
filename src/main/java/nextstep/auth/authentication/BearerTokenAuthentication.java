package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthentication implements AuthenticationStrategy {

    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthentication(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (jwtTokenProvider.validateToken(requestToken)) {
            String email = jwtTokenProvider.getPrincipal(requestToken);
            List<String> roles = jwtTokenProvider.getRoles(requestToken);

            Authentication authentication = new Authentication(email, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    }
}
