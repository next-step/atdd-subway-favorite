package nextstep.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.GithubService;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubService githubService;

    @PostMapping("/github/authorization")
    public ResponseEntity<GithubAccessTokenResponse> getAuth(
            @RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok(githubService.getAuth(request));
    }

    @GetMapping("/github/profile")
    public ResponseEntity<GithubAccessTokenResponse> getProfile() {
        return null;
    }

}
