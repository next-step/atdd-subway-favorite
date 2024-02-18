package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2Response;

public class MockUserDetailsService implements UserDetailsService {
    private final String userPassword;

    public MockUserDetailsService(final String userPassword) {
        this.userPassword = userPassword;
    }

    public UserDetail loadUserByEmail(final String email) {
        return new UserDetail(1L, email, userPassword);
    }

    public UserDetail loadOrCreateUser(final OAuth2Response oAuth2Response) {
        return new UserDetail(1L, oAuth2Response.getEmail(), userPassword);
    }
}
