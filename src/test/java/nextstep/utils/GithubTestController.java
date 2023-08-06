package nextstep.utils;

import nextstep.auth.token.oauth2.github.GithubAccessTokenRequest;
import nextstep.auth.token.oauth2.github.GithubAccessTokenResponse;
import nextstep.auth.token.oauth2.github.GithubProfileResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

import static nextstep.utils.GithubTestController.GithubResponses.*;

@RestController
@Profile("test")
public class GithubTestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest githubAccessTokenRequest) {
        String code = githubAccessTokenRequest.getCode();
        GithubResponses githubResponse = getGithubResponseWithCode(code);
        GithubAccessTokenResponse result = new GithubAccessTokenResponse();

        switch (githubResponse) {
            case 사용자1:
                result.setAccessToken(사용자1.getAccessToken());
                break;
            case 사용자2:
                result.setAccessToken(사용자2.getAccessToken());
                break;
            case 사용자3:
                result.setAccessToken(사용자3.getAccessToken());
                break;
            case 사용자4:
                result.setAccessToken(사용자4.getAccessToken());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + githubResponse);
        }

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(@RequestHeader("Authorization") String token) {
        String tokenValue = token.split(" ")[1];
        GithubResponses githubResponse = getGithubResponseWithToken(tokenValue);
        GithubProfileResponse result = new GithubProfileResponse();
        final int testAge = 10;

        switch (githubResponse) {
            case 사용자1:
                result.setEmail(사용자1.getEmail());
                break;
            case 사용자2:
                result.setEmail(사용자2.getEmail());
                break;
            case 사용자3:
                result.setEmail(사용자3.getEmail());
                break;
            case 사용자4:
                result.setEmail(사용자4.getEmail());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + githubResponse);
        }
        result.setAge(testAge);
        return ResponseEntity.ok().body(result);
    }

    public enum GithubResponses {
        사용자1("aofijeowifjaoief", "access_token_1", "email1@email.com"),
        사용자2("fau3nfin93dmn", "access_token_2", "email2@email.com"),
        사용자3("afnm93fmdodf", "access_token_3", "email3@email.com"),
        사용자4("fm04fndkaladmd", "access_token_4", "email4@email.com");

        private String code;
        private String accessToken;
        private String email;

        GithubResponses(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static GithubResponses getGithubResponseWithToken(String token) {
            for (GithubResponses githubResponses : values()) {
                if(githubResponses.getAccessToken().equals(token)) {
                    return githubResponses;
                }
            }
            throw new NoSuchElementException();
        }

        public static GithubResponses getGithubResponseWithCode(String code) {
            for (GithubResponses githubResponses : values()) {
                if(githubResponses.getCode().equals(code)) {
                    return githubResponses;
                }
            }
            throw new NoSuchElementException();
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
    }
}
