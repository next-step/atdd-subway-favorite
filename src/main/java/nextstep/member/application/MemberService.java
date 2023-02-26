package nextstep.member.application;

import java.util.Optional;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public Member createMember(String email) {
        return memberRepository.save(new Member(email));
    }

    public MemberResponse findMember(Long id) {
        Member member = findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findById(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
