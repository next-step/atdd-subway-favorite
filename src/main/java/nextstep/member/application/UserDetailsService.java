package nextstep.member.application;

import nextstep.member.domain.LoginMember;

@FunctionalInterface
public interface UserDetailsService {
  LoginMember loadUserByUsername(String email);
}
