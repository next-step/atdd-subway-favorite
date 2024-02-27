package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;

import java.util.Optional;

public interface MemberService {

    MemberResponse createMember(MemberRequest request);

    MemberResponse findMember(Long id);

    void updateMember(Long id, MemberRequest param);

    void deleteMember(Long id);

    MemberResponse findMe(LoginMember loginMember);

    Member findMemberByEmailOrElseThrow(String email);

    Optional<Member> findMemberByEmail(String email);
}
