package nextstep.github.ui;

import nextstep.github.application.GithubService;
import nextstep.github.application.dto.GithubAccessTokenRequest;
import nextstep.github.application.dto.GithubAccessTokenResponse;
import nextstep.github.application.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/github")
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
    @GetMapping("/login/oauth/authorize")
    public ResponseEntity<String> getCode(@RequestParam("client_id") String clientId) {
        return ResponseEntity.ok(githubService.getCode(clientId));
    }

    /**
     * 엑세스 토큰 조회
     * @param githubAccessTokenRequest
     * @return
     */
    @PostMapping("/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        GithubAccessTokenResponse githubAccessTokenResponse = githubService.getAccessToken(githubAccessTokenRequest);
        return ResponseEntity.ok(githubAccessTokenResponse);
    }

    /**
     * 프로필 조회
     * @param request
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<GithubProfileResponse> getProfile(HttpServletRequest request) {
        String principal = (String) request.getAttribute("principal");
        return ResponseEntity.ok(githubService.getProfile(principal));
    }
}
