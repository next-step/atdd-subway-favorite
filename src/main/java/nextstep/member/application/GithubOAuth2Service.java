package nextstep.member.application;

import nextstep.member.domain.LoginMember;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.GithubOAuth2Client;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.payload.AccessTokenResponse;
import nextstep.member.payload.GithubUserInfoResponse;
import nextstep.security.service.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GithubOAuth2Service {

    private final MemberRepository memberRepository;
    private final GithubOAuth2Client githubOAuth2Client;
    private final JwtTokenProvider<LoginMember> jwtTokenProvider;

    public GithubOAuth2Service(final MemberRepository memberRepository, final GithubOAuth2Client githubOAuth2Client, final JwtTokenProvider<LoginMember> jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.githubOAuth2Client = githubOAuth2Client;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(final String code) {
        AccessTokenResponse accessToken = githubOAuth2Client.getAccessToken(code);
        GithubUserInfoResponse userInfo = githubOAuth2Client.getUserInfo(accessToken.getAccess_token());

        Member member = memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(new Member(userInfo.getEmail())));

        return new TokenResponse(jwtTokenProvider.createToken(new LoginMember(member.getId(), member.getEmail())));
    }

}
