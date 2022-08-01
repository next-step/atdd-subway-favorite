package nextstep.auth.filter;

import nextstep.member.domain.LoginMember;

public interface LoginService {
    LoginMember loadUserByUsername(String username);

    boolean isUserExist(String username);
}
