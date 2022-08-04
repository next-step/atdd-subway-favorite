package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.stereotype.Component;

@Component
public class JpaAuthenticationManager implements AuthenticationManager {

    private final LoginMemberService loginMemberService;

    public JpaAuthenticationManager(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }


    @Override
    public Authentication load(String principal, String credentials) {
        final LoginMember loginMember = loginMemberService.loadUserByUsername(principal);
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (loginMember.isInvalidPassword(credentials)) {
            throw new AuthenticationException();
        }
        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }
}
