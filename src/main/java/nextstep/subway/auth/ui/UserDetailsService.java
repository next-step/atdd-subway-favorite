package nextstep.subway.auth.ui;

import nextstep.subway.auth.domain.User;

public interface UserDetailsService {
    User loadUserByUsername(String email);
}
