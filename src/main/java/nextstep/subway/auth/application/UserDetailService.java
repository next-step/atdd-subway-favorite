package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.UserDetail;

@FunctionalInterface
public interface UserDetailService {
    UserDetail loadUserByUsername(String userName);
}
