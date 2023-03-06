package nextstep.subway.fake;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GithubFakeController {

    @PostMapping("/fake/github/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        String accessToken = GithubFakeResponses.fromCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }

    @GetMapping("/fake/github/profile")
    public ResponseEntity<GithubProfileResponse> getUsersProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubFakeResponses response = GithubFakeResponses.fromAccessToken(accessToken);
        return ResponseEntity.ok(new GithubProfileResponse(response.getId(), response.getEmail()));
    }
}
