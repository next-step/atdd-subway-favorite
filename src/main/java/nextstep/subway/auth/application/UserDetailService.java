package nextstep.subway.auth.application;

import nextstep.subway.auth.dto.UserDetail;

public interface UserDetailService {
    UserDetail loadUserByUsername(String email);
}
