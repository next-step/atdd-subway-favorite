package nextstep.github.ui;

import nextstep.github.application.GithubService;
import nextstep.github.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/login")
public class GithubController {
    private GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping("/github")
    public ResponseEntity<TokenResponse> createLoginGithubToken (@RequestBody GithubAccessTokenRequest request) throws URISyntaxException {
        try {
            return ResponseEntity.ok().body(githubService.login(request.getCode()));
        } catch (Exception e) {
            URI redirectUri = new URI("/login/github/join");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return ResponseEntity.notFound().headers(httpHeaders).build();
        }
    }

    @GetMapping("/github/join")
    public ResponseEntity<TokenResponse> joinGithub() {
        return ResponseEntity.ok().body(githubService.join());
    }
}
