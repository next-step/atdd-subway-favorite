package nextstep.github.ui;

import nextstep.github.application.GithubService;
import nextstep.github.application.dto.GithubAccessTokenRequest;
import nextstep.github.application.dto.GithubAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github/login/oauth")
public class GithubController {
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    /**
     * 권한 증서 조회
     * @param clientId
     * @return
     */
    @GetMapping("/authorize")
    public ResponseEntity<String> getCode(@RequestParam("client_id") String clientId) {
        return ResponseEntity.ok(githubService.getCode(clientId));
    }

    /**
     * 엑세스 토큰 조회
     * @param githubAccessTokenRequest
     * @return
     */
    @PostMapping("/access_token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        GithubAccessTokenResponse githubAccessTokenResponse = githubService.getAccessToken(githubAccessTokenRequest);
        return ResponseEntity.ok(githubAccessTokenResponse);
    }
}
