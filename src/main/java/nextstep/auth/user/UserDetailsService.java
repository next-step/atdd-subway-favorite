package nextstep.auth.user;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {
    public LoginMember loadUserByUsername(String username);
}
