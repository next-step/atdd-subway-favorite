package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final AuthMemberLoader authMemberLoader;
    public UsernamePasswordAuthenticationFilter(AuthMemberLoader authMemberLoader) {
        this.authMemberLoader = authMemberLoader;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            final AuthMember loginMember = findLoginMember(request);
            setAuthentication(loginMember);
        }
        return true;
    }

    private AuthMember findLoginMember(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final AuthMember authMember = authMemberLoader.loadUserByUsername(username);
        checkPassword(password, authMember);
        return authMember;
    }

    private void checkPassword(String password, AuthMember authMemberLoader) {
        if (!authMemberLoader.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }

    private void setAuthentication(AuthMember loginMember) {
        final Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
