package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;

public interface UserDetailService {

    UserDetail getUser(String email);

    UserDetail getOrCreateUser(String email, String code, Integer age);
}
