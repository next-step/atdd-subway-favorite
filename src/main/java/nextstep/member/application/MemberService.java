package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.infrastructure.GithubProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    private final JwtTokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional
    public Member createMember(GithubProfileResponse githubProfile) {
        return memberRepository.save(githubProfile.toMember());
    }

    public MemberResponse findMember(Long id) {
        Member member = findMemberById(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberByToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException();
        }
        String email = tokenProvider.getPrincipal(token);
        Member member = findMemberByEmail(email).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(RuntimeException::new);
    }

    public Optional<Member> findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
