package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            LoginMember loginMember = findLoginMemberr(request);
            setAuthentication(loginMember);
        }
        return true;
    }

    private LoginMember findLoginMemberr(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        LoginMember loginMember = loginMemberService.loadUserByUsername(username);

        checkPassword(password, loginMember);
        return loginMember;
    }

    private void checkPassword(String password, LoginMember loginMember) {
        if (!loginMember.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }

    private void setAuthentication(LoginMember loginMember) {
        Authentication authentication = new Authentication(loginMember.getEmail(),
            loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
