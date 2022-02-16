package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());

        return MemberResponse.of(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberOfMine(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(RuntimeException::new);

        return MemberResponse.of(member);
    }

    public MemberResponse updateMemberOfMine(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(RuntimeException::new);
        member.update(memberRequest.toMember());

        return MemberResponse.of(member);
    }

    public void deleteMemberOfMine(LoginMember loginMember) {

    }
}