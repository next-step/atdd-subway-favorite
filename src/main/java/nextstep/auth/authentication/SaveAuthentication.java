package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;

public class SaveAuthentication {

    private final String principal;

    private final String credentials;

    private final LoginMember loginMember;

    public SaveAuthentication(String principal, String credentials, LoginMember loginMember) {
        this.principal = principal;
        this.credentials = credentials;
        this.loginMember = loginMember;
    }

    public void execute() {
        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
