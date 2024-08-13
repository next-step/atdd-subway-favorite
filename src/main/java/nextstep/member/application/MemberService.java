package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.presentation.MemberRequest;
import nextstep.auth.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.MemberRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMe(String email) {
        return memberRepository.findByEmail(email)
                .map(it -> MemberResponse.of(it))
                .orElseThrow(RuntimeException::new);
    }
}
