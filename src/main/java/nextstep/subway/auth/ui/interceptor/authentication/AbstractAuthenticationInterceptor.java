package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.AuthenticationProvider;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

    protected final AuthenticationProvider authenticationProvider;

    public AbstractAuthenticationInterceptor(AuthenticationProvider authenticationProvider) {

        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        applyAuthentication(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    /**
     * 인증 요청을 확인 및 적용한다.
     * //TODO 성공, 실패 처리하는 handler를 분리하면 좋겠지만 지금으로서는 over engineering 일 수도 있어서 놔둬보자
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void applyAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
