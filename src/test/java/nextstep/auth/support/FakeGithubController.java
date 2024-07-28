package nextstep.auth.support;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.domain.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FakeGithubController {
  @PostMapping("/github/login/oauth/access_token")
  public ResponseEntity<GithubAccessTokenResponse> getAccessToken(
      @RequestBody GithubAccessTokenRequest request) {
    String code = request.getCode();
    String accessToken = GithubResponses.findByCode(code).getAccessToken();
    GithubAccessTokenResponse response =
        new GithubAccessTokenResponse(accessToken, "repo,gist", "bearer");
    return ResponseEntity.ok(response);
  }

  @GetMapping("/github/user")
  public ResponseEntity<GithubProfileResponse> getUserProfile(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
    String accessToken = authorization.split(" ", 2)[1];
    Member member = GithubResponses.findByAccessToken(accessToken).getMember();
    GithubProfileResponse response = new GithubProfileResponse("", "", member.getEmail());
    return ResponseEntity.ok(response);
  }
}
