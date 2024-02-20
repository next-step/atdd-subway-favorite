package nextstep.subway.member.application;

import nextstep.subway.auth.AuthenticationException;
import nextstep.subway.auth.application.AuthManager;
import nextstep.subway.auth.application.TokenType;
import nextstep.subway.member.application.dto.TokenResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final MemberService memberService;
    private final AuthManager authManager;

    public LoginService(MemberService memberService,
                        AuthManager authManager) {
        this.memberService = memberService;
        this.authManager = authManager;
    }

    public TokenResponse createGithubToken(String email,
                                           String password) {
        Member member = memberService.findMemberByEmail(email);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        String token = authManager.createToken(member.getEmail(), TokenType.JWT);

        return new TokenResponse(token);
    }

    public TokenResponse createGithubToken(String code) {
        String accessToken = authManager.createToken(code, TokenType.GITHUB);

        String email = authManager.getPrincipal(accessToken, TokenType.GITHUB);
        memberService.findMemberByEmailNotExistSave(email);

        return new TokenResponse(accessToken);
    }
}
