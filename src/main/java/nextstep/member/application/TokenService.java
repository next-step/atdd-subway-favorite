package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.GithubClient;
import nextstep.auth.GithubProfileResponse;
import nextstep.member.AuthenticationException;
import nextstep.member.OAuth2User;
import nextstep.member.OAuth2UserService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final OAuth2UserService oAuth2UserService;

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfile = githubClient.requestGithubProfile(githubToken);

        OAuth2User oAuth2User = oAuth2UserService.loadUser(githubProfile);
        Member member = memberService.findOrCreateMemberFromOAuth2User(oAuth2User);
        String token = jwtTokenProvider.createToken(member.getId(), member.getEmail());
        return new TokenResponse(token);
    }
}
