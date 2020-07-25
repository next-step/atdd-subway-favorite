package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.application.SecurityContextPersistenceHandler;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextPersistenceInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService, SecurityContextPersistenceHandler persistenceHandler) {
        super(persistenceHandler);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 얘랑 session persistence랑 하나로 합칠 수 있을 듯 한데...
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!isValidToken(token)) {
            return true;
        }

        String payload = jwtTokenProvider.getPayload(token);

        UserDetails principal = extractPrincipal(payload);

        // TODO 직접 참조하지 말고 인증 성공 실패에 대한 핸들러를 제공해보자
        persistenceHandler.persist(new SecurityContext(new Authentication(principal)));

        return true;
    }

    private UserDetails extractPrincipal(String payload) throws Exception {
        return userDetailsService.convertJsonToUserDetail(payload);
    }

    private boolean isValidToken(String token) {
        // TODO 라이브러리에 너무 의존하고 있는 느낌. Provider로 빼보자
        return jwtTokenProvider.validateToken(token);
    }
}
