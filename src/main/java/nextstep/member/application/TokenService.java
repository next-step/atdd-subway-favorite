package nextstep.member.application;

import nextstep.exception.FailIssueAccessTokenException;
import nextstep.member.AuthenticationException;
import nextstep.member.application.request.GetAccessTokenRequest;
import nextstep.member.application.response.GetAccessTokenResponse;
import nextstep.member.application.response.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final String TOKEN_ISSUE_FAIL_MESSAGE = "ACCESS TOKEN ISSUE FAILED";

    private MemberService memberService;
    private JwtTokenProvider jwtTokenProvider;
    private GithubClient githubClient;

    public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubClient = githubClient;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public GetAccessTokenResponse getAccessToken(GetAccessTokenRequest getAccessTokenRequest) {
        String accessCode = githubClient.requestGithubToken(getAccessTokenRequest.getCode());

        if (accessCode.equals(TOKEN_ISSUE_FAIL_MESSAGE)) {
            throw new FailIssueAccessTokenException();
        }

        return GetAccessTokenResponse.from(accessCode);
    }

}
