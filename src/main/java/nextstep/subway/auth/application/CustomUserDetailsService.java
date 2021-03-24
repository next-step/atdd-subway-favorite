package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String email);
}
