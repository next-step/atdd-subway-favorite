package nextstep.auth.authentication;

import nextstep.auth.User;

public interface UserDetailsService {
    User loadUserByUsername(String email);
}
