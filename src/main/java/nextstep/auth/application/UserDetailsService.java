package nextstep.auth.application;

import java.util.Optional;

public class UserDetailsService {
    public Optional<UserDetail> loadUserByEmail(final String email) {
        return Optional.of(new UserDetail());
    }
}
