package nextstep.subway.member.application;

import nextstep.subway.member.domain.LoginMember;

public interface UserDetailsService {

    LoginMember loadUserByUsername(String email);
}
