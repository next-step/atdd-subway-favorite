package nextstep.subway.auth.infrastructure;

import nextstep.subway.auth.domain.User;

@FunctionalInterface
public interface UserDetailService {
    User loadUserByUsername(String username);
}
