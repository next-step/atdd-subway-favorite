package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.User;

public interface UserDetailsService {
    User loadUserByUsername(String username);
}
