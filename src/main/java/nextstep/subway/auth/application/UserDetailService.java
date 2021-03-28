package nextstep.subway.auth.application;

import nextstep.subway.member.domain.LoginMember;

public interface UserDetailService {
    public LoginMember loadUserByUsername(String email);
}
