package nextstep.subway.utils;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.*;

@RestController
@RequestMapping("login")
public class GithubController {

    @PostMapping("oauth/authorize")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody final GithubAccessTokenRequest request) {
        final String accessToken = GithubResponses.findCode(request.getCode()).getAccessToken();
        return ResponseEntity.ok(new GithubAccessTokenResponse(accessToken));
    }

    @GetMapping("oauth/access_token")
    public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader(AUTHORIZATION) final String authorization) {
        final String accessToken = authorization.split(" ")[1];
        final String email = GithubResponses.findAccessToken(accessToken).getEmail();
        return ResponseEntity.ok(new GithubProfileResponse(email));
    }
}
