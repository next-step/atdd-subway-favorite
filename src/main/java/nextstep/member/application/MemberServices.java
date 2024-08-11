package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;

import java.util.Optional;

public interface MemberServices {

    MemberResponse createMember(MemberRequest memberRequest);

    MemberResponse findMember(Long id);

    void updateMember(Long id, MemberRequest memberRequest);

    void deleteMember(Long id);

    Member findMemberByEmail(String email);

    Optional<Member> findMemberOptionalByEmail(String email);

    MemberResponse findMe(LoginMember loginMember);
}

