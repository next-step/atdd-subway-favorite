package nextstep.auth.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;

import java.util.Optional;

public interface UserDetailService {
    Member findMemberByEmailOrElseThrow(String email);

    Optional<Member> findMemberByEmail(String email);

    MemberResponse createMember(MemberRequest request);
}
