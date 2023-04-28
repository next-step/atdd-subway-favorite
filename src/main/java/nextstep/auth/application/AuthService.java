package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.exception.InvalidSigninInformation;
import nextstep.github.GithubClient;
import nextstep.github.application.dto.GithubAccessTokenResponse;
import nextstep.github.application.dto.GithubProfileResponse;
import nextstep.github.application.dto.GithubRequest;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;


    public TokenResponse signIn(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.
                getEmail()).orElseThrow(() -> new InvalidSigninInformation("email", "가입된 이메일이 존재하지 않습니다."));
        if (!member.checkPassword(tokenRequest.getPassword())) {
            throw new InvalidSigninInformation("password", "비밀번호가 올바르지 않습니다.");

        }
        String token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse signInByGithub(final GithubRequest githubRequest) {
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(githubRequest.getCode());
        GithubProfileResponse githubProfileFromGithub = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);
        Member member = memberRepository.findByEmail(githubProfileFromGithub.getEmail())
                .orElse(memberRepository.save(new Member(githubProfileFromGithub.getEmail())));
        return new GithubAccessTokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
    }

}
