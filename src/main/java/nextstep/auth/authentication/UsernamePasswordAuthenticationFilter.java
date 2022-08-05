package nextstep.auth.authentication;

import java.util.Map;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private static final String USER_NAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String userName = parameterMap.get(USER_NAME_FIELD)[0];
        String password = parameterMap.get(PASSWORD_FIELD)[0];

        AuthenticationToken token = new AuthenticationToken(userName, password);

        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
