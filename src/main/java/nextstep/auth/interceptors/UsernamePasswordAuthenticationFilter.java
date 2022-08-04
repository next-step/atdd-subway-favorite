package nextstep.auth.interceptors;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationProvidingInterceptor {

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter("username");
        String credentials = request.getParameter("password");
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public void authenticate(LoginMember loginMember, HttpServletResponse response) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
