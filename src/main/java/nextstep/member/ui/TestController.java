package nextstep.member.ui;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.domain.GithubResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/github/login")
    public ResponseEntity<GithubAccessTokenResponse> accessToken(
        @RequestBody GithubAccessTokenRequest tokenRequest) {

        GithubResponses response = GithubResponses.getResponsesByCode(
            tokenRequest.getCode());
        return ResponseEntity.ok(new GithubAccessTokenResponse(response.getAccessToken()));
    }

    @GetMapping("/github/user")
    public ResponseEntity<GithubProfileResponse> user(
        @RequestHeader("Authorization") String authorization) {
        GithubProfileResponse response = new GithubProfileResponse("email@email.com", 20);
        return ResponseEntity.ok(response);
    }
}