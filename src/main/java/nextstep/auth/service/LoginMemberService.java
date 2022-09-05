package nextstep.auth.service;

import nextstep.member.domain.LoginMember;

public interface LoginMemberService {
    LoginMember loadUserByUsername(String email);
}
