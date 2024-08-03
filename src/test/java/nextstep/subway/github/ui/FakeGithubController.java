package nextstep.subway.github.ui;

import nextstep.subway.github.application.dto.GithubAccessTokenRequest;
import nextstep.subway.github.application.dto.GithubAccessTokenResponse;
import nextstep.subway.github.application.dto.GithubProfileResponse;
import nextstep.subway.utils.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static nextstep.subway.utils.GithubResponses.*;
import static nextstep.subway.utils.GithubResponses.토큰으로_사용자_조회;

@RestController
public class FakeGithubController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
            @RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses 깃허브_사용자 = 코드로_사용자_조회(tokenRequest.getCode());

        String accessToken = 깃허브_사용자.getAccessToken();

        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];

        GithubResponses 깃허브_사용자 = 토큰으로_사용자_조회(accessToken);

        GithubProfileResponse response = new GithubProfileResponse(깃허브_사용자.getEmail());
        return ResponseEntity.ok(response);
    }
}
