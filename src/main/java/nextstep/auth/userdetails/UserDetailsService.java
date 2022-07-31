package nextstep.auth.userdetails;

import nextstep.member.domain.LoginMember;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String username);

}
