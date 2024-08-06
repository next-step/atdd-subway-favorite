package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.GithubOAuth2Client;
import nextstep.member.domain.GithubOAuthProperty;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.payload.AccessTokenRequest;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GithubOAuth2Service {

    private final MemberRepository memberRepository;
    private final GithubOAuth2Client githubOAuth2Client;
    private final JwtTokenProvider jwtTokenProvider;

    private final GithubOAuthProperty githubOAuthProperty;


    public GithubOAuth2Service(final MemberRepository memberRepository, final GithubOAuth2Client githubOAuth2Client, final JwtTokenProvider jwtTokenProvider, final GithubOAuthProperty githubOAuthProperty) {
        this.memberRepository = memberRepository;
        this.githubOAuth2Client = githubOAuth2Client;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubOAuthProperty = githubOAuthProperty;
    }

    public TokenResponse createToken(final String code) {
        AccessTokenRequest accessTokenRequest = AccessTokenRequest.builder()
                .clientId(githubOAuthProperty.getClientId())
                .clientSecret(githubOAuthProperty.getClientSecret())
                .code(code)
                .build();

        AccessTokenResponse accessToken = githubOAuth2Client.getAccessToken(accessTokenRequest);
        GithubUserInfoResponse userInfo = githubOAuth2Client.getUserInfo(accessToken.getAccess_token());

        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() ->
                        memberRepository.save(new Member(userInfo.getEmail()))
                );

        return new TokenResponse(jwtTokenProvider.createToken(member.getId(), member.getEmail()));
    }

}
