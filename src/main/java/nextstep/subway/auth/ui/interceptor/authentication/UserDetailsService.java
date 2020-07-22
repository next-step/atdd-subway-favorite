package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.member.domain.LoginMember;

public interface UserDetailsService {
    LoginMember loadUserByUsername(String principal);
}
