package nextstep.auth.authentication;

import nextstep.member.domain.LoginMember;

public interface UserDetailService {
    LoginMember loadUserByUsername(String email);
}
