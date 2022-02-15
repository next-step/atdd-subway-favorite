package nextstep.auth.model.authorization.interceptor;

import nextstep.auth.model.authentication.UserDetails;
import nextstep.auth.model.authentication.service.UserDetailsService;
import nextstep.auth.model.authorization.AuthorizationExtractor;
import nextstep.auth.model.authorization.AuthorizationType;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

public class TokenSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        return extractSecurityContext(extractJwtToken(request));
    }

    private String extractJwtToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    private SecurityContext extractSecurityContext(String jwtToken) {
        try {
            String email = jwtTokenProvider.getPayload(jwtToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            return SecurityContext.from(new Authentication(userDetails));
        } catch (Exception e) {
            return null;
        }
    }
}
