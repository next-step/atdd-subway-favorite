package nextstep.auth.authentication;

import java.util.Map;
import java.util.Objects;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            final AuthenticationToken token = new AuthenticationToken(username, password);

            final LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

            nullValidation(loginMember);

            loginMember.checkPassword(token.getCredentials());

            final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void nullValidation(final LoginMember loginMember) {
        if (Objects.isNull(loginMember)) {
            throw new AuthenticationException();
        }
    }

}
