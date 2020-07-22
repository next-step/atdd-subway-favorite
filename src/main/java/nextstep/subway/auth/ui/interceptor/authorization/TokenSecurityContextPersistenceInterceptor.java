package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.application.SecurityContextPersistenceHandler;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.ObjectMapperUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, SecurityContextPersistenceHandler persistenceHandler) {
        super(persistenceHandler);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 얘랑 session persistence랑 하나로 합칠 수 있을 듯 한데...
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!isValidToken(token)) {
            return true;
        }

        String payload = jwtTokenProvider.getPayload(token);

        LoginMember principal = extractPrincipal(payload);

        // TODO 직접 참조하지 말고 인증 성공 실패에 대한 핸들러를 제공해보자
        persistenceHandler.persist(new SecurityContext(new Authentication(principal)));

        return true;
    }

    private LoginMember extractPrincipal(String payload) throws Exception {
        MemberResponse memberResponse = ObjectMapperUtils.convert(payload, MemberResponse.class);

        // TODO LoginMember로 들어온다고 가정하다보니까 payload가 유연하지 않음. Session Interceptor에서도 무조건 LoginMember를 넣고 있는데 일괄적으로 변경 필요
        return new LoginMember(memberResponse.getId(), memberResponse.getEmail(), null, memberResponse.getAge());
    }

    private boolean isValidToken(String token) {
        // TODO 라이브러리에 너무 의존하고 있는 느낌. Provider로 빼보자
        return jwtTokenProvider.validateToken(token);
    }
}
