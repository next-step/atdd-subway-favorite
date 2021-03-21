package nextstep.subway.auth.infrastructure;

import nextstep.subway.member.domain.LoginMember;

@FunctionalInterface
public interface UserDetailService {
    LoginMember loadUserByUsername(String username);
}
