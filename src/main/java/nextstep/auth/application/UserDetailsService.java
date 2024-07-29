package nextstep.auth.application;

import nextstep.auth.domain.UserDetails;
import nextstep.auth.exception.UsernameNotFoundException;

public interface UserDetailsService {
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
