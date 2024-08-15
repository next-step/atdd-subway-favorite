package nextstep.member;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.unit.GithubUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubUser userFixture = GithubUser.valueOfCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(userFixture.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubUser userFixture = GithubUser.valueOfAccessToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(userFixture.getEmail(), null);
        return ResponseEntity.ok(response);
    }
}
