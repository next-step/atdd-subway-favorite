package nextstep.member.tobe.application;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.tobe.application.dto.TokenRequest;
import nextstep.member.tobe.application.dto.TokenResponse;
import nextstep.member.tobe.domain.AuthService;
import nextstep.member.tobe.domain.GithubClient;

@Service
@Qualifier("githubAuthService")
public class GithubAuthService implements AuthService {
    private final String clientId;
    private final String clientSecret;
    private final GithubClient githubClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public GithubAuthService(
        @Value("${github.client-id}") String clientId,
        @Value("${github.client-secret}") String clientSecret,
        GithubClient githubClient,
        JwtTokenProvider jwtTokenProvider,
        MemberService memberService
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.githubClient = githubClient;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public TokenResponse login(TokenRequest request) {
        GithubAccessTokenRequest githubAccessTokenRequest = GithubAccessTokenRequest.of(clientId, clientSecret,
            request.getCode(), null, null);
        GithubAccessTokenResponse accessTokenResponse = githubClient.getAccessToken(githubAccessTokenRequest);
        GithubProfileResponse profile = githubClient.getUserProfile(accessTokenResponse.getAccessToken());

        Member member = memberService.findMemberByEmailOrNull(profile.getEmail());
        if (member == null) {
            MemberRequest memberRequest = new MemberRequest(profile.getEmail(), "default_password", profile.getAge());
            member = memberService.createMember(memberRequest).toMember();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());
        return TokenResponse.of(token);
    }
}
