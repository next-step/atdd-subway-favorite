package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.annotation.AuthHeader;
import nextstep.auth.domain.GithubAuthService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubResponses;
import nextstep.util.AuthUtil;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/test/github")
@RestController
public class GithubTestController {

    private final GithubAuthService githubAuthType;

    @PostMapping("access-token")
    public GithubAccessTokenResponse createAccessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubResponses githubResponses = GithubResponses.ofCode(request.getCode());
        return GithubAccessTokenResponse.of(githubResponses.getAccessToken());
    }

    @GetMapping("profile")
    public GithubProfileResponse getProfile(@AuthHeader String header) {
        String accessToken = AuthUtil.parseAccessToken(header, githubAuthType.getPrefix());
        GithubResponses githubResponses = GithubResponses.ofAccessToken(accessToken);
        return GithubProfileResponse.of(githubResponses.getEmail());
    }
}
