package nextstep.auth.authentication;

import nextstep.auth.user.User;

public class ValidateUser {

    public void execute(String credentials, User user) {

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!user.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

    }
}
