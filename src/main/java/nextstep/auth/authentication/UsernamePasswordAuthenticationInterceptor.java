package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

public class UsernamePasswordAuthenticationInterceptor extends AuthenticationNonChainInterceptor {
    public UsernamePasswordAuthenticationInterceptor(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    protected UserInformation createPrincipal(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new UserInformation(email, password);
    }

    @Override
    protected void afterAuthentication(HttpServletResponse response, LoginMember loginMember) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
