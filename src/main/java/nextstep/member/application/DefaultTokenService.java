package nextstep.member.application;

import nextstep.member.AuthenticationException;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.GithubClient;
import nextstep.member.domain.Member;
import nextstep.member.domain.TokenService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultTokenService implements TokenService {
    private final GithubClient githubClient;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final String clientId;
    private final String clientSecret;

    public DefaultTokenService(GithubClient githubClient, MemberService memberService, JwtTokenProvider jwtTokenProvider,
        @Value("${github.client-id}") String clientId,
        @Value("${github.client-secret}") String clientSecret) {
        this.githubClient = githubClient;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public TokenResponse createToken(String email, String password) {
        Member member = memberService.findMemberByEmailOrThrow(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createTokenFromGithub(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(clientId, clientSecret, code, null, null);
        GithubAccessTokenResponse accessTokenResponse = githubClient.getAccessToken(githubAccessTokenRequest);
        GithubProfileResponse profile = githubClient.getUserProfile(accessTokenResponse.getAccessToken());

        Member member = memberService.findMemberByEmailOrNull(profile.getEmail());
        if (member == null) {
            MemberRequest memberRequest = new MemberRequest(profile.getEmail(), "default_password", profile.getAge());
            member = memberService.createMember(memberRequest).toMember();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }
}
