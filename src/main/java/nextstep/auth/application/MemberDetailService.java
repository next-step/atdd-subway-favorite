package nextstep.auth.application;

import nextstep.member.domain.Member;

public interface MemberDetailService {
    Member findOrElseGet(String email);

    Member findByEmail(String email);
}
