package nextstep.auth.ui;

import nextstep.auth.application.GithubResponses;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubEmailResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nextstep.auth.application.GithubResponses.findByAccessToken;
import static nextstep.auth.application.GithubResponses.findByCode;

@RestController
public class GithubTestController {
    @PostMapping("/github/login/oauth/access_token")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(@RequestBody GithubAccessTokenRequest tokenRequest) {
        GithubResponses responses = findByCode(tokenRequest.getCode());
        GithubAccessTokenResponse response = new GithubAccessTokenResponse(responses.getAccessToken(), "", "", "");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/github/email")
    public ResponseEntity<List<GithubEmailResponse>> user(@RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.split(" ")[1];
        GithubResponses responses = findByAccessToken(accessToken);
        GithubEmailResponse response = new GithubEmailResponse(responses.getEmail(), true, true, "");
        return ResponseEntity.ok(List.of(response));
    }
}
