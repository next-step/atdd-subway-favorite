package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GitHubLoginRequest;
import nextstep.auth.application.dto.GitHubLoginTokenResponse;
import nextstep.auth.application.dto.GitHubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class GitHubLoginService {

    private final GitHubClient githubClient;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public GitHubLoginService(GitHubClient githubClient, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public GitHubLoginTokenResponse createToken(GitHubLoginRequest request) {
        String accessToken = githubClient.requestGitHubAccessToken(request.getCode());
        GitHubProfileResponse profile = githubClient.requestGithubProfile(accessToken);

        if (memberRepository.existsByEmail(profile.getEmail())) {
            Member member = findMemberByEmail(profile.getEmail());
            String token = createToken(member.getEmail(), member.getPassword());
            return new GitHubLoginTokenResponse(token);
        }

        Member member = memberRepository.save(new Member(profile.getEmail(), "password", profile.getAge()));
        String token = createToken(member.getEmail(), member.getPassword());
        return new GitHubLoginTokenResponse(token);
    }

    private String createToken(String email, String password) {
        Member member = findMemberByEmail(email);

        if (member.checkPassword(password)) {
            return  jwtTokenProvider.createToken(member.getEmail());
        }

        throw new AuthenticationException();
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원이 없습니다. email: " + email));
    }
}
