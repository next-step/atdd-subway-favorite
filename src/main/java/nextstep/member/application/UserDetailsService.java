package nextstep.member.application;

import nextstep.member.domain.User;

public interface UserDetailsService {

    User loadUserByUsername(String email);
}
