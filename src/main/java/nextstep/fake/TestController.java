package nextstep.fake;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/github/access-token")
    public ResponseEntity<GithubAccessTokenResponse> test(@RequestBody GithubAccessTokenRequest request) {
        return ResponseEntity.ok(new GithubAccessTokenResponse(GithubResponse.findByCode(request.getCode()).getAccessToken()));
    }
}
