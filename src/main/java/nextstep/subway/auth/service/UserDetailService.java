package nextstep.subway.auth.service;

import nextstep.subway.auth.domain.UserDetail;

public interface UserDetailService {
    UserDetail loadUserByUsername(String email);
}
