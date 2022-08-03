package nextstep.auth.authentication.nonchain;

import nextstep.auth.context.SecurityContextMapper;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String userEmail = request.getParameter("username");
        LoginMember loginUser = loginMemberService.loadUserByUsername(userEmail);

        SecurityContextMapper.setContext(loginUser.getEmail(), loginUser.getAuthorities());
        return false;
    }
}
