package nextstep.member.application;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String email, String password) {
        Member member = memberRepository.findMemberByEmailAndPassword(email, password);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findByToken(String token) {
        String principal = null;
        if (jwtTokenProvider.validateToken(token)) {
            principal = jwtTokenProvider.getPrincipal(token);
        }
        System.out.println("principal = " + principal);
        Member member = memberRepository.findByEmail(principal).orElseThrow(IllegalArgumentException::new);
        return MemberResponse.of(member);
    }
}