package nextstep.subway.utils;

import nextstep.member.application.LoginService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FakeLoginController {

    @Autowired
    private FakeLoginService loginService;

    @PostMapping("/fake/login/github")
    public ResponseEntity<TokenResponse> createGithubToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        String token = loginService.createGithubToken(tokenRequest.getCode());
        System.out.println("accessToken = " + token);
        return ResponseEntity.ok().body(new TokenResponse(token));
    }
}
