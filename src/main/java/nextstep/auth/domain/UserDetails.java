package nextstep.auth.domain;

import nextstep.member.domain.LoginMember;

public interface UserDetails {
    void checkPassword(String password);

    LoginMember toLoginMember();
}
