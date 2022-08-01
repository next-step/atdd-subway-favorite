package nextstep.auth.user;

import nextstep.member.domain.LoginMember;

/**
 * @author a1101466 on 2022/08/01
 * @project subway
 * @description
 */
public interface UserDetailsService {
    LoginMember loadUserByUsername(String username);
}
