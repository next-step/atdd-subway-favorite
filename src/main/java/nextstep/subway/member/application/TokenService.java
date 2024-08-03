package nextstep.subway.member.application;

import nextstep.subway.dto.GithubProfileResponse;
import nextstep.subway.dto.OauthGithubTokenRequest;
import nextstep.subway.entity.GithubClient;
import nextstep.subway.exception.NoSuchMemberException;
import nextstep.subway.member.AuthenticationException;
import nextstep.subway.member.application.dto.MemberRequest;
import nextstep.subway.member.application.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmailOrThrow(email);

        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        return new TokenResponse(jwtTokenProvider.createToken(member.getEmail()));
    }

    public TokenResponse createGithubToken(OauthGithubTokenRequest request) {
        String githubAccessToken = githubClient.requestGithubToken(request.getCode());

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUserInfo(githubAccessToken);
        String email = githubProfileResponse.getEmail();

        try {
            memberService.findMemberByEmailOrThrow(email);
        } catch (NoSuchMemberException e) {
            memberService.createMember(new MemberRequest(email));
        }

        return new TokenResponse(jwtTokenProvider.createToken(email));
    }
}
