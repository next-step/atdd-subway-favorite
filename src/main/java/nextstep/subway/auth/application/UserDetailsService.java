package nextstep.subway.auth.application;

import nextstep.subway.auth.dto.UserDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email);
}
