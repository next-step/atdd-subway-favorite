package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.GithubAccessToken;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubResponses;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/test/github")
@RestController
public class GithubTestController {

    @PostMapping("access-token")
    public GithubAccessTokenResponse createAccessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.fromCode(request.getCode());
        return GithubAccessTokenResponse.from(githubResponses.getAccessToken());
    }

    @GetMapping("profile")
    public GithubProfileResponse getProfile(@GithubAccessToken String accessToken) {
        GithubResponses githubResponses = GithubResponses.fromAccessToken(accessToken);
        return GithubProfileResponse.from(githubResponses.getEmail());
    }
}
