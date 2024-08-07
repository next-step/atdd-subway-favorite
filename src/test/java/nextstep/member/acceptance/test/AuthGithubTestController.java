package nextstep.member.acceptance.test;

import nextstep.auth.application.dto.github.GithubAccessTokenRequest;
import nextstep.auth.application.dto.github.GithubAccessTokenResponse;
import nextstep.auth.application.dto.github.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Profile("test")
@RestController
@RequestMapping("/login")
public class AuthGithubTestController {

    // github accessToken 을 받기 위한 테스트 컨트롤러
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String code = tokenRequest.getCode();

        String accessToken = GithubUser.getTokenByCode(code);
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    // github resource 를 받기 위한 테스트 컨트롤러
    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];

        GithubUser user = GithubUser.findUserByAccessToken(accessToken);

        GithubProfileResponse response = new GithubProfileResponse(user.getEmail(), user.getAge());
        return ResponseEntity.ok(response);
    }

}
