package nextstep.member.ui;

import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TokenController {

    private  final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse tokenResponse = tokenService.login(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }



    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        MemberResponse member = tokenService.getMember(accessToken);
        return ResponseEntity.ok().body(member);
    }
}
