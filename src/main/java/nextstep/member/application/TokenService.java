package nextstep.member.application;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.common.exception.ErrorResponse;
import nextstep.member.common.exception.LoginException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class TokenService {

    private final JwtTokenProvider tokenProvider;

    private final MemberRepository memberRepository;

    private final GithubClient githubClient;

    public TokenService(JwtTokenProvider tokenProvider, MemberRepository memberRepository, GithubClient githubClient) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
        this.githubClient = githubClient;
    }

    public TokenResponse login(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();

        Member member = memberRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new LoginException(ErrorResponse.INVALIDATION_LOGIN_INFORMATION));
        String token = tokenProvider.createToken(email, member.getRoles());

        return new TokenResponse(token);
    }

    public MemberResponse getMember(String token) {
        String email = tokenProvider.getPrincipal(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new LoginException(ErrorResponse.NOT_FOUND_EMAIL));
        return MemberResponse.of(member);
    }

    public GithubProfileResponse getGitHubMember(String token) {
        return githubClient.getGithubProfileFromGithub(token);
    }

    public TokenResponse login(String code) {

        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(code);
        GithubProfileResponse githubProfileFromGithub = getGitHubMember(accessTokenFromGithub);
        String email = githubProfileFromGithub.getEmail();

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new LoginException(ErrorResponse.NOT_FOUND_EMAIL));

        return new TokenResponse(tokenProvider.createToken(email, member.getRoles()));
    }
}
