package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.LoginMemberService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private final LoginMemberService loginMemberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // TODO: 구현하세요.
        return true;
    }
}
