package nextstep.subway.auth.application;

import nextstep.subway.member.domain.LoginMember;

@FunctionalInterface
public interface UserDetailService {
    public LoginMember loadUserByUsername(String userName);
}
