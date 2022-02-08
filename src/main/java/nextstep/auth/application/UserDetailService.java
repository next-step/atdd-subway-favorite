package nextstep.auth.application;

import nextstep.auth.application.dto.User;

public interface UserDetailService {

    User loadUserByUsername(String email);

}
