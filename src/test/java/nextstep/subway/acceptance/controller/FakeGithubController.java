package nextstep.subway.acceptance.controller;

import nextstep.GithubAccessTokenRequest;
import nextstep.GithubAccessTokenResponse;
import nextstep.GithubProfileResponse;
import org.apache.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FakeGithubController {
    @PostMapping("/fake/token")
    public GithubAccessTokenResponse token(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses user = GithubResponses.fromCode(request.getCode());
        return new GithubAccessTokenResponse(user.getAccessToken());
    }

    @GetMapping("/fake/profile")
    public GithubProfileResponse profile(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
        GithubResponses user = GithubResponses.fromAccessToken(accessToken);
        return new GithubProfileResponse(user.getEmail());
    }
}
