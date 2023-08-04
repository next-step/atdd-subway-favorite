package nextstep.auth.util;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody final GithubAccessTokenRequest request) {
        Optional<VirtualUser> user = VirtualUser.getUserByCode(request.getCode());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
        }

        GithubAccessTokenResponse response = new GithubAccessTokenResponse(user.get().getToken(), "bearer", "repo,gist", "");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") final String accessToken) {
        Optional<VirtualUser> user = VirtualUser.getUserToken(accessToken);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!user.get().isValid()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        GithubProfileResponse response = new GithubProfileResponse(user.get().getEmail(), user.get().getAge());
        return ResponseEntity.ok().body(response);
    }
}

