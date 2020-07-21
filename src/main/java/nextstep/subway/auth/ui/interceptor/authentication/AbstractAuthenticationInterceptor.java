package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    public AbstractAuthenticationInterceptor(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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

    protected Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        // TODO UserDesignService를 직접 참고하기보다는 Auth Provider같은걸로 리팩토링을 끼얹나
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    protected void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
