package nextstep.auth.application;

import nextstep.auth.application.dto.OAuth2Response;

public interface UserDetailsService {
    UserDetail loadUserByEmail(final String email);

    UserDetail loadOrCreateUser(final OAuth2Response oAuth2Response);
}
