package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.message.Message;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubClient githubClient;

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_EXIST_EAMIL.getMessage()));
        if (!member.arePasswordsSame(tokenRequest.getPassword())) {
            throw new IllegalArgumentException(Message.INVALID_PASSWORD.getMessage());
        }
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
        return new TokenResponse(token);
    }

    public GithubAccessTokenResponse getGithubToken(String code) {
        String accessToken = githubClient.getAccessTokenFromGithub(code);
        return new GithubAccessTokenResponse(accessToken);
    }

}
