package nextstep.test;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.ui.dto.GithubProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {
        String accessToken = "";
        switch (tokenRequest.getCode()) {
            case "aofijeowifjaoief":
                accessToken = GithubResponses.사용자1.getAccessToken();
                break;
            case "fau3nfin93dmn":
                accessToken = GithubResponses.사용자2.getAccessToken();
                break;
            case "afnm93fmdodf":
                accessToken = GithubResponses.사용자3.getAccessToken();
                break;
            case "fm04fndkaladmd":
                accessToken = GithubResponses.사용자4.getAccessToken();
                break;
        }
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(accessToken, "", "");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
        return ResponseEntity.ok(response);
    }
}

