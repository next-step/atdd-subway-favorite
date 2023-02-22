package nextstep.member.ui;

import nextstep.member.application.dto.github.GithubAccessTokenRequest;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.application.dto.github.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Profile("test")
@RequestMapping("/github-fake")
@RestController
public class GitHubFakeController {
    private static final int AUTH_TOKEN_INDEX = 1;

    @PostMapping("/access-token")
    public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
        GithubProfileResponse response = GithubResponses.find(githubResponse -> githubResponse.code.equals(request.getCode()));
        return ResponseEntity.ok(new GithubAccessTokenResponse(response.getAccessToken()));
    }

    @GetMapping("/profile")
    public ResponseEntity<GithubProfileResponse> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null || !authorization.startsWith("token ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorization.split(" ")[AUTH_TOKEN_INDEX];
        GithubProfileResponse response = GithubResponses.find(githubResponse -> githubResponse.accessToken.equals(token));
        return ResponseEntity.ok(response);
    }

    public enum GithubResponses {
        관리자("abcdefghijklmnop", "access_token", "admin@email.com"),
        사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

        private final String code;
        private final String accessToken;
        private final String email;

        GithubResponses(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getEmail() {
            return email;
        }

        public static GithubProfileResponse find(Predicate<GithubResponses> predicate) {
            return Stream.of(values())
                    .filter(predicate)
                    .map(response -> new GithubProfileResponse(response.code, response.accessToken, response.email))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }
}
