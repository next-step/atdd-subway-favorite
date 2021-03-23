package nextstep.subway.auth.application;

import nextstep.subway.member.domain.LoginMember;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String email);
}