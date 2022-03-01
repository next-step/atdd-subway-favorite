package nextstep.auth.application;

import nextstep.auth.domain.UserDetail;
import nextstep.member.domain.LoginMember;

public interface UserDetailService {
	UserDetail loadUserByUsername(String email);
}
