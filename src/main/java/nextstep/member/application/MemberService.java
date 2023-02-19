package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private MemberRepository memberRepository;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
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

    public MemberResponse findMember(String authorization) {
        String token = jwtTokenProvider.parseJwt(authorization);
        String principal = jwtTokenProvider.getPrincipal(token);
        return findByUserEmail(principal).map(MemberResponse::of).orElseThrow();
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Optional<Member> findByUserEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
