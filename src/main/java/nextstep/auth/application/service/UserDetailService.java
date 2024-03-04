package nextstep.auth.application.service;

import nextstep.auth.domain.UserDetail;

public interface UserDetailService {
	UserDetail getUserDetailByEmail(String email);
	UserDetail createUserIfNotExist(UserDetail userDetail);
}
