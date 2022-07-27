package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginMember loginMember = findLoginMember(request);
        setAuthentication(loginMember);

        return true;
    }

    private LoginMember findLoginMember(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        LoginMember loginMember = loginMemberService.loadUserByUsername(username);
        if (isNotMatchPassword(password, loginMember)) {
            throw new AuthenticationException();
        }
        return loginMember;
    }

    private boolean isNotMatchPassword(String password, LoginMember loginMember) {
        return !loginMember.checkPassword(password);
    }

    private void setAuthentication(LoginMember loginMember) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
