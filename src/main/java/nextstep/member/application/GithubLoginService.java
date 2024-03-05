package nextstep.member.application;

import nextstep.member.application.dto.OAuth2Request;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.ui.dto.GithubProfileResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GithubLoginService {

    private final OAuth2Client githubClient;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // bean name
    public GithubLoginService(@Qualifier("githubClient") OAuth2Client githubClient,
        MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.githubClient = githubClient;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(OAuth2Request oAuth2Request) {
        String accessToken = githubClient.requestAccessToken(oAuth2Request.getCode());
        GithubProfileResponse githubProfile = githubClient.requestUserInfo(accessToken);
        Member member = memberRepository.findByEmail(githubProfile.getEmail())
            .orElseGet(() -> memberRepository.save(new Member(githubProfile.getEmail(), "", 0)));
        String token = jwtTokenProvider.createToken(member.getEmail());
        return new TokenResponse(token);
    }
}
