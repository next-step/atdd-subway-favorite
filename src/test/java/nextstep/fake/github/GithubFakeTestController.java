package nextstep.fake.github;


import nextstep.member.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GithubFakeTestController {

    @PostMapping("fake/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubStaticUsers githubUser = GithubStaticUsers.getByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(
                githubUser.getAccessToken(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        return ResponseEntity.ok(response);
    }
}
