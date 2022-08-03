package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String userEmail = request.getParameter("username");
        LoginMember loginUser = loginMemberService.loadUserByUsername(userEmail);

        Authentication authentication = new Authentication(loginUser.getEmail(), loginUser.getAuthorities());
        SecurityContext securityContext = new SecurityContext(authentication);
        SecurityContextHolder.setContext(securityContext);
        return true;
    }
}
