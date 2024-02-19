package nextstep.subway.member.application;

import nextstep.subway.member.AuthenticationException;
import nextstep.subway.member.application.dto.TokenResponse;
import nextstep.subway.member.client.ExternalTokenFetcher;
import nextstep.subway.member.client.github.GithubTokenFetcher;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ExternalTokenFetcher externalTokenFetcher;

    public TokenService(MemberService memberService,
                        JwtTokenProvider jwtTokenProvider,
                        GithubTokenFetcher externalTokenFetcher) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.externalTokenFetcher = externalTokenFetcher;
    }

    public TokenResponse createToken(String email,
                                     String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createToken(String code) {
        String token = externalTokenFetcher.requestToken(code);
        return new TokenResponse(token);
    }
}
