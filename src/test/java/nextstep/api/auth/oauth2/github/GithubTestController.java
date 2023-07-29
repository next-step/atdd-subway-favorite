package nextstep.api.auth.oauth2.github;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenRequest;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubAccessTokenResponse;
import nextstep.api.auth.application.token.oauth2.github.dto.GithubProfileResponse;

@RestController
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody final GithubAccessTokenRequest request) {
        final var user = VirtualUsers.toVirtualUser(request);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final var response = user.get().toAccessTokenResponse();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") final String accessToken) {
        final var user = VirtualUsers.toVirtualUser(accessToken);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final var response = user.get().toProfileResponse();
        return ResponseEntity.ok().body(response);
    }
}
