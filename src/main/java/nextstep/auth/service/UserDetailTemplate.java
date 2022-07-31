package nextstep.auth.service;

import nextstep.auth.authentication.AuthenticationException;

public abstract class UserDetailTemplate {

    public void validate(UserDetails loginMember, String inputPassword) {
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(inputPassword)) {
            throw new AuthenticationException();
        }
    }
}
