package nextstep.subway.member.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.TokenManager;
import nextstep.auth.application.TokenType;
import nextstep.subway.member.application.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final MemberService memberService;
    private final TokenManager tokenManager;

    public TokenService(MemberService memberService,
                        TokenManager tokenManager) {
        this.memberService = memberService;
        this.tokenManager = tokenManager;
        ;
    }

    public TokenResponse createGithubToken(String email,
                                           String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = tokenManager.createToken(member.getEmail(), TokenType.JWT);

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = tokenManager.createToken(code, TokenType.GITHUB);

        String email = tokenManager.getPrincipal(accessToken, TokenType.GITHUB);
        memberService.findMemberByEmailNotExistSave(email);

        return new TokenResponse(accessToken);
    }
}
