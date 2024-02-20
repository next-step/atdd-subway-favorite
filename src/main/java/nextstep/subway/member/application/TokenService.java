package nextstep.subway.member.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.TokenProvider;
import nextstep.auth.client.ExternalTokenFetcher;
import nextstep.auth.client.dto.ProfileResponse;
import nextstep.subway.member.application.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final ExternalTokenFetcher externalTokenFetcher;

    public TokenService(MemberService memberService,
                        TokenProvider tokenProvider,
                        ExternalTokenFetcher externalTokenFetcher) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.externalTokenFetcher = externalTokenFetcher;
    }

    public TokenResponse createToken(String email,
                                     String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = tokenProvider.createToken(member.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse createToken(String code) {
        String accessToken = externalTokenFetcher.requestToken(code);

        ProfileResponse userResponse = externalTokenFetcher.findUser(accessToken);
        memberService.findMemberByEmailNotExistSave(userResponse);

        return new TokenResponse(accessToken);
    }
}
