package nextstep.auth.interceptor.nonchain;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationChainInterceptor {
    private static final String NOT_MATCH_EMAIL_PASSWORD = "이메일과 비밀번호가 일치하지 않습니다.";

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected MemberInfo createPrincipal(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new MemberInfo(email, password);
    }

    @Override
    protected void afterValidation(HttpServletResponse response, UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
