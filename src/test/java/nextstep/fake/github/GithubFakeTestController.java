package nextstep.fake.github;


import nextstep.member.infrastructure.github.dto.GithubAccessTokenRequest;
import nextstep.member.infrastructure.github.dto.GithubAccessTokenResponse;
import nextstep.member.infrastructure.github.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("fake/github/user")
    public ResponseEntity<GithubProfileResponse> getUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        GithubStaticUsers githubUser = GithubStaticUsers.getByAccessToken(authorization);
        GithubProfileResponse response = new GithubProfileResponse(
                123123L,
                UUID.randomUUID().toString(),
                githubUser.getEmail(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());

        return ResponseEntity.ok(response);
    }
}
