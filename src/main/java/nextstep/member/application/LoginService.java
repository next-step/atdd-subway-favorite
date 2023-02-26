package nextstep.member.application;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubProfileResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.NotAuthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final GithubClient githubClient;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberService memberService, GithubClient githubClient) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.githubClient = githubClient;
    }

    public TokenResponse authorize(TokenRequest request) {
        Member findMember = memberService.findByEmail(request.getEmail());

        if (!findMember.checkPassword(request.getPassword())) {
            throw new NotAuthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(findMember.getEmail(), findMember.getRoles());
        return TokenResponse.of(token);
    }

    public TokenResponse authorize(GithubAccessTokenRequest request) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(request.getCode())
            .orElseThrow(() -> new NotAuthorizedException("인증정보가 유효하지 않습니다."));
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        String githubEmail = githubProfile.getEmail();
        Member member = memberService.findOrCreateMember(githubEmail);
        return TokenResponse.of(jwtTokenProvider.createToken(githubEmail, member.getRoles()));
    }
}
