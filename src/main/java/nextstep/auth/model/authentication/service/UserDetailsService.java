package nextstep.auth.model.authentication.service;

import nextstep.auth.model.authentication.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
