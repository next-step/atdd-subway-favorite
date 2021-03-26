package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
