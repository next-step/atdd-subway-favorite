package nextstep.member.ui;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.utils.FixtureUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @PostMapping("/github/login/oauth/access_token")
  public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
    final var response = FixtureUtil.getBuilder(GithubAccessTokenResponse.class)
        .set(javaGetter(GithubAccessTokenResponse::getAccessToken), request.getCode() + "_access_token")
        .sample();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/github/user")
  public ResponseEntity<GithubProfileResponse> getUserProfile(@RequestHeader("Authorization") String authorization) {
    final var accessToken = authorization.split(" ")[1];
    final var email = accessToken.replace("_access_token", "@gmail.com");

    final var response = FixtureUtil.getBuilder(GithubProfileResponse.class)
        .set("email", email, 20)
        .sample();
    return ResponseEntity.ok(response);
  }
}
