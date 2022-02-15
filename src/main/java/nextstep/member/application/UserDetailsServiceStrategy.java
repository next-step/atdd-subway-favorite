package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.domain.LoginMember;

public interface UserDetailsServiceStrategy {
    LoginMember authenticate(String principal, AuthenticationToken token);
}
