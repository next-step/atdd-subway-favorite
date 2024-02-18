package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubService {
    private final GithubClient githubClient;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    public GithubService(final GithubClient githubClient, final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.githubClient = githubClient;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final String code) {
        final String githubToken = githubClient.requestGithubToken(code);
        final GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(githubToken);

        final MemberResponse memberResponse = memberService.findMemberOrCreate(githubProfileResponse);

        String token = jwtTokenProvider.createToken(memberResponse.getEmail());

        return new TokenResponse(token);
    }
}
