package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    private JwtTokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findMemberById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException();
        }
        String email = tokenProvider.getPrincipal(token);
        Member member = findMemberByEmail(email);
        return MemberResponse.of(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(RuntimeException::new);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(RuntimeException::new);
    }
}
