package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("이메일로 회원을 찾을 수 업습니다. " + email));
    }

    public String jwtLogin(String email, String password) {
        Member member = findByEmailAndPassword(email, password);
        return jwtTokenProvider.createToken(email, member.getRoles());
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional
    public String githubLogin(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse response = githubClient.getGithubProfileFromGithub(accessToken);
        updateAccessToken(response.getEmail(), accessToken);
        return accessToken;
    }

    private void updateAccessToken(String email, String accessToken) {
        Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        member.updateAccessToken(accessToken);
    }
}