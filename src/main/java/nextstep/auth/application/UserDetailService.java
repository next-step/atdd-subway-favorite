package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;

public interface UserDetailService {
    UserDetail findUser(String email);

    UserDetail findOrCreateUser(String email, Integer age);
}
