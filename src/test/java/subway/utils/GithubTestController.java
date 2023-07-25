package subway.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import subway.auth.token.oauth2.github.GithubAccessTokenResponse;
import subway.auth.token.oauth2.github.GithubProfileResponse;

import java.util.Map;

@RestController
public class GithubTestController {

    /**
     * GithubClient.getAccessTokenFromGithub()
     */
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        GithubResponses responseByCode = GithubResponses.findByCode(code);
        GithubAccessTokenResponse response = GithubAccessTokenResponse.builder()
                .accessToken(responseByCode.getAccessToken())
                .tokenType("none")
                .scope("none")
                .bearer("none.none.none")
                .build();
        return ResponseEntity.ok().body(response);
    }

    /**
     * GithubClient.getGithubProfileFromGithub()
     */
    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader(value = "Authorization") String authorization) {
        String token = authorization.split(" ")[1];
        GithubResponses responseByToken = GithubResponses.findByAccessToken(token);

        GithubProfileResponse build = GithubProfileResponse.builder()
                .age(20)
                .email(responseByToken.getEmail())
                .build();
        return ResponseEntity.ok().body(build);
    }
}

