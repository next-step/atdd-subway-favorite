package nextstep.utils.fakeMock;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.utils.dtoMock.GithubResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = GithubResponse.getAccessTokenByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<ProfileResponse> user(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authorization.split(" ")[1];
        String email = GithubResponse.getEmailByAccessToken(accessToken);
        int age = GithubResponse.getAgeByAccessToken(accessToken);
        ProfileResponse response = new ProfileResponse(email, age);
        return ResponseEntity.ok(response);
    }
}

