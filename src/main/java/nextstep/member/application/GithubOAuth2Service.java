package nextstep.member.application;

import nextstep.member.domain.GithubOAuth2ClientImpl;
import nextstep.member.domain.LoginMember;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.security.oauth2.AccessTokenResponse;
import nextstep.security.oauth2.GithubOauth2Client;
import nextstep.security.oauth2.GithubUserInfoResponse;
import nextstep.security.application.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GithubOAuth2Service {

    private final MemberRepository memberRepository;
    private final GithubOauth2Client githubOauth2Client;
    private final JwtTokenProvider<LoginMember> jwtTokenProvider;

    public GithubOAuth2Service(final MemberRepository memberRepository, final GithubOauth2Client githubOauth2Client, final JwtTokenProvider<LoginMember> jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.githubOauth2Client = githubOauth2Client;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final String code) {
        AccessTokenResponse accessToken = githubOauth2Client.getAccessToken(code);
        GithubUserInfoResponse userInfo = githubOauth2Client.getUserInfo(accessToken.getAccess_token());

        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(userInfo.getEmail())));

        return new TokenResponse(jwtTokenProvider.createToken(new LoginMember(member.getId(), member.getEmail())));
    }

}
