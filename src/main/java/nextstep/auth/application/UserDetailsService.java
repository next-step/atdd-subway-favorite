package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2Response;

import java.util.Optional;

public class UserDetailsService {
    public Optional<UserDetail> loadUserByEmail(final String email) {
        return Optional.of(new UserDetail());
    }

    public UserDetail loadOrCreateUser(final OAuth2Response oAuth2Response) {
        return new UserDetail();
    }
}
