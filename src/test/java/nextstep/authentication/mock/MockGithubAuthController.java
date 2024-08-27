package nextstep.authentication.mock;

import nextstep.authentication.application.dto.GithubAccessTokenRequest;
import nextstep.authentication.application.dto.GithubAccessTokenResponse;
import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.utils.UserInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MockGithubAuthController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = UserInformation.lookUpAccessToken(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        UserInformation userInfo = UserInformation.lookUp(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(userInfo.getEmail(), userInfo.getAge());
        return ResponseEntity.ok(response);
    }
}
