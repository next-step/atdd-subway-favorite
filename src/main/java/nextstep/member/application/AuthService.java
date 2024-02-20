package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final TokenService tokenService;
    private GithubClient githubClient;

    public AuthService(TokenService tokenService, GithubClient githubClient, MemberService memberService){
        this.tokenService = tokenService;
        this.githubClient = githubClient;
        this.memberService = memberService;
    }

    public TokenResponse login(String email, String password) {
        return new TokenResponse(tokenService.createToken(email, password).getAccessToken());
    }

    public TokenResponse loginGithub(String code) {
        final String accessToken = githubClient.requestGithubToken(code);
        final GithubProfileResponse profile = githubClient.requestGithubProfile(accessToken);

        memberService.findOrCreateMember(new MemberRequest(profile.getEmail(), code, profile.getAge()));

        return new TokenResponse(tokenService.createToken(profile.getEmail(), code).getAccessToken());
    }
}
