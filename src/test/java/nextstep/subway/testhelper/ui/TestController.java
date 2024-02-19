package nextstep.subway.testhelper.ui;

import nextstep.subway.member.client.dto.GithubAccessTokenRequest;
import nextstep.subway.member.client.dto.GithubAccessTokenResponse;
import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponsesFixture responsesFixture = GithubResponsesFixture.findByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(responsesFixture.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }
}
