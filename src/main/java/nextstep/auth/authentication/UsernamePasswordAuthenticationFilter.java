package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginMember member;

        try {
            member = loginMemberService.loadUserByUsername(email);
        } catch (RuntimeException e) {
            throw new AuthenticationException();
        }

        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(member.getEmail(), member.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
