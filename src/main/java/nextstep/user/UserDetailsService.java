package nextstep.user;

import nextstep.auth.authentication.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
