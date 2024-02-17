package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.domain.OAuth2User;
import nextstep.auth.domain.OAuth2UserRequest;
import nextstep.auth.application.dto.TokenResponse;
import nextstep.auth.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final LoginMemberService loginMemberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;
    private final OAuth2UserService oAuth2UserService;

    public TokenResponse createToken(String email, String password) {
        LoginMember loginMember = loginMemberService.loadMember(email, password);

        String token = jwtTokenProvider.createToken(loginMember.getId(), loginMember.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromOAuth2User(String code, String provider) {
        OAuth2UserRequest oAuth2UserRequest;
        switch (provider) {
            case "github":
                oAuth2UserRequest = getGithubProfile(code);
                break;
            default:
                throw new AuthenticationException();
        }

        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        LoginMember loginMember = loginMemberService.loadMember(oAuth2User);
        String token = jwtTokenProvider.createToken(loginMember.getId(), loginMember.getEmail());
        return new TokenResponse(token);
    }

    private OAuth2UserRequest getGithubProfile(String code) {
        String githubToken = githubClient.requestGithubToken(code);
        GithubProfileResponse githubProfile = githubClient.requestGithubProfile(githubToken);

        return githubProfile;
    }

//    public TokenResponse createTokenFromGithub(String code) {
//        String githubToken = githubClient.requestGithubToken(code);
//        GithubProfileResponse githubProfile = githubClient.requestGithubProfile(githubToken);
//
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(githubProfile);
//        LoginMember loginMember = loginMemberService.loadMember(oAuth2User);
//        String token = jwtTokenProvider.createToken(loginMember.getId(), loginMember.getEmail());
//        return new TokenResponse(token);
//    }
}
