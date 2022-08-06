package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.User;

public class SaveAuthentication {

    private final String principal;

    private final String credentials;

    private final User user;

    public SaveAuthentication(String principal, String credentials, User user) {
        this.principal = principal;
        this.credentials = credentials;
        this.user = user;
    }

    public void execute() {
        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!user.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(user.getEmail(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
