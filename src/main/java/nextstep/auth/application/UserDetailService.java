package nextstep.auth.application;

import nextstep.member.domain.LoginMember;

public interface UserDetailService {

    LoginMember loadUserByUsername(String email);

}
