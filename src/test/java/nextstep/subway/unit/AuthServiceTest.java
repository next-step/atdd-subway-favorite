package nextstep.subway.unit;

import nextstep.member.application.AuthService;
import nextstep.member.application.dto.GithubTokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @DisplayName("GitHub을 통해 토큰을 생성한다.")
    @Test
    void createGitHubToken() {
        // when
        TokenResponse response = authService.createTokenWithGithub(new GithubTokenRequest(GithubResponses.사용자1.getCode()));

        // then
        assertThat(response.getAccessToken()).isNotBlank();
    }
}
