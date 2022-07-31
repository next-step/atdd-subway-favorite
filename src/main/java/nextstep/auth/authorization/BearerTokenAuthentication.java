package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthentication implements AuthorizationStrategy {

    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthentication(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void authorize(HttpServletRequest request) {
        String requestToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (jwtTokenProvider.validateToken(requestToken)) {
            String email = jwtTokenProvider.getPrincipal(requestToken);
            List<String> roles = jwtTokenProvider.getRoles(requestToken);

            Authentication authentication = new Authentication(email, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
