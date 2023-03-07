package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(JwtTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
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

    public MemberResponse findMember(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByToken(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new RuntimeException();
        }

        String email = tokenProvider.getPrincipal(accessToken);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);

        return MemberResponse.of(member);
    }

}