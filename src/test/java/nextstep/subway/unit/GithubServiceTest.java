package nextstep.subway.unit;

import nextstep.member.application.GithubService;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.domain.stub.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import({GithubService.class, JwtTokenProvider.class})
@DataJpaTest
public class GithubServiceTest {
    @Autowired
    GithubService githubService;

    @Test
    @DisplayName("Authorization Fake 서버 테스트 : 정상")
    void authorization_fake_server_test_success() {
        GithubResponses 사용자1 = GithubResponses.사용자1;
        GithubAccessTokenResponse response = githubService.getAuth(new GithubAccessTokenRequest(사용자1.getCode(), 사용자1.getEmail(), 사용자1.getAccessToken()));
        assertThat(response.getAccessToken()).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("Authorization Fake 서버 테스트 : 존재하지 않는 코드")
    void authorization_fake_server_test_fail() {
        GithubAccessTokenResponse auth = githubService.getAuth(new GithubAccessTokenRequest("Invlid", "invalid@email.com", "access_token_invalid"));
        assertThat(auth.getAccessToken()).isEqualTo("invalid_token");
    }
}
