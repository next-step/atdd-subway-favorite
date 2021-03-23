package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.AuthMember;

public interface UserDetailsService {

    AuthMember loadUserByUsername(String email);
}
