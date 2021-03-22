package nextstep.subway.member.application;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(memberRequest.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        member.update(memberRequest.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberOfMine(LoginMember loginMember) {
        return findMember(loginMember.getId());
    }

    public void updateMemberOfMine(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(RuntimeException::new);
        member.update(memberRequest.toMember());
    }

    public void deleteMemberOfMine(LoginMember loginMember) {
        memberRepository.deleteById(loginMember.getId());
    }
}