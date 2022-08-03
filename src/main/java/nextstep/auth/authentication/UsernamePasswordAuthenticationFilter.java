package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NonChainFilter {
    private static final String NOT_MATCH_EMAIL_PASSWORD = "이메일과 비밀번호가 일치하지 않습니다.";

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService) {
        super(loginMemberService);
    }

    @Override
    protected MemberInfo createPrincipal(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new MemberInfo(email, password);
    }

    @Override
    protected void afterValidation(HttpServletResponse response, LoginMember loginMember) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
