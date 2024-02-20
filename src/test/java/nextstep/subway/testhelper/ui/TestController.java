package nextstep.subway.testhelper.ui;

import nextstep.subway.auth.client.github.dto.GithubAccessTokenRequest;
import nextstep.subway.auth.client.github.dto.GithubAccessTokenResponse;
import nextstep.subway.auth.client.github.dto.GithubProfileResponse;
import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponsesFixture responsesFixture = GithubResponsesFixture.findByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(responsesFixture.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponsesFixture responsesFixture = GithubResponsesFixture.findByToken(accessToken);
        GithubProfileResponse response = new GithubProfileResponse(responsesFixture.getEmail(), responsesFixture.getAge());
        return ResponseEntity.ok(response);
    }
}
