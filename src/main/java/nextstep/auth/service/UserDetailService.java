package nextstep.auth.service;

import nextstep.auth.domain.UserDetail;

public interface UserDetailService {
    UserDetail loadUserByUsername(String email);
}
