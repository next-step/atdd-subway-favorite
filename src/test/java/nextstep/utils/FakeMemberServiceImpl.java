package nextstep.utils;

import nextstep.member.application.UserDetailsService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;

import java.util.Optional;

import static nextstep.utils.GithubResponse.사용자1;

public class FakeMemberServiceImpl implements UserDetailsService {
    private final Member member = Member.of(1L, 사용자1.getEmail(), "password", 사용자1.getAge());
    private final MemberResponse memberResponse = MemberResponse.of(member);

    @Override
    public MemberResponse createMember(MemberRequest memberRequest) {
        return memberResponse;
    }

    @Override
    public MemberResponse findMember(Long id) {
        return memberResponse;
    }

    @Override
    public void updateMember(Long id, MemberRequest memberRequest) {

    }

    @Override
    public void deleteMember(Long id) {

    }

    @Override
    public Member findMemberByEmail(String email) {
        return member;
    }

    @Override
    public Optional<Member> findMemberOptionalByEmail(String email) {
        return Optional.of(member);
    }

    @Override
    public MemberResponse findMe(LoginMember loginMember) {
        return memberResponse;
    }
}
