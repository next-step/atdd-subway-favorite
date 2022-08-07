package nextstep.auth.authentication.chain;


import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextMapper;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter implements AuthenticationChainFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if(!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        Authentication authenticate = authenticate(token);

        SecurityContextMapper.setContext(authenticate.getPrincipal().toString(), authenticate.getAuthorities() );

        return true;
    }

    public Authentication authenticate(String token) {
        String userName = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);
        return new Authentication(userName, roles);
    }
}
