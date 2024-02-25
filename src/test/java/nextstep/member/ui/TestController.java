package nextstep.member.ui;

import static com.navercorp.fixturemonkey.api.experimental.JavaGetterMethodPropertySelector.javaGetter;

import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.utils.FixtureUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @PostMapping("/github/login/oauth/access_token")
  public ResponseEntity<GithubAccessTokenResponse> getAccessToken(@RequestBody GithubAccessTokenRequest request) {
    final var response = FixtureUtil.getBuilder(GithubAccessTokenResponse.class)
        .set(javaGetter(GithubAccessTokenResponse::getAccessToken), request.getCode() + " access token")
        .sample();
    return ResponseEntity.ok(response);
  }
}
