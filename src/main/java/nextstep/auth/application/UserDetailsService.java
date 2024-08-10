package nextstep.auth.application;

import nextstep.auth.domain.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
