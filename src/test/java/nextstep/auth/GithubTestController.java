package nextstep.auth;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public GithubAccessTokenResponse accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest
    ) {
        return new GithubAccessTokenResponse(
            GithubResponses.findByCode(tokenRequest.getCode())
                .getAccessToken()
        );
    }

    @GetMapping("/github/user")
    public GithubProfileResponse user(
        @RequestHeader("Authorization") String authorization
    ) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses githubUser = GithubResponses.findByAccessToken(accessToken);
        return new GithubProfileResponse(githubUser.getEmail(), githubUser.getAge());
    }
}
