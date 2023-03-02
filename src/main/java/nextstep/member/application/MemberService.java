package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthMemberService;
import nextstep.auth.dto.AuthMember;
import nextstep.exception.AuthenticationException;
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
public class MemberService implements AuthMemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    @Transactional
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

    public String jwtLogin(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("이메일로 회원을 찾을 수 업습니다. " + email));

        if (!member.checkPassword(password)) {
            throw new AuthenticationException("유효하지 않은 비밀번호 입니다.");
        }

        return jwtTokenProvider.createToken(email, member.getRoles());
    }

    @Transactional
    public String githubLogin(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse response = githubClient.getGithubProfileFromGithub(accessToken);
        updateAccessToken(response.getEmail(), accessToken);
        return accessToken;
    }

    private void updateAccessToken(String email, String accessToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> new Member(email, accessToken));

        member.updateAccessToken(accessToken);
    }

    @Override
    public AuthMember findJwtMember(String token) {
        String principal = jwtTokenProvider.getPrincipal(token);
        return memberRepository.findByEmail(principal)
                .map(AuthMember::of)
                .orElseThrow(() -> new IllegalArgumentException("이메일로 회원을 찾을 수 업습니다. " + principal));
    }

    @Override
    public AuthMember findGitHubMember(String token) {
        return memberRepository.findByAccessToken(token)
                .map(AuthMember::of)
                .orElseThrow(IllegalArgumentException::new);
    }
}