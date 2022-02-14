package nextstep.auth.authentication.application;

import nextstep.auth.authentication.domain.UserDetail;

public interface UserDetailsService {
    UserDetail loadUserByUsername(String email);
}
