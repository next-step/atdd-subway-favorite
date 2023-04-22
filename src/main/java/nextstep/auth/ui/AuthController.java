package nextstep.auth.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.AuthService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/token")
    public TokenResponse login(@RequestBody TokenRequest tokenRequest) {
        Member member = authService.signIn(tokenRequest);
        String jws = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRoles());
        return new TokenResponse(jws);
    }
}
