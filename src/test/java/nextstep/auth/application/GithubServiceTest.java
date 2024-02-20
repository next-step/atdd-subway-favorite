package nextstep.auth.application;

import nextstep.auth.application.dto.TokenResponse;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.auth.acceptance.AuthSteps.createToken;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubServiceTest {
    @Autowired
    private GithubService githubService;

    @DisplayName("code를 통한 Github Login, 이미 등록된 회원일 경우")
    @Test
    void createTokenByGithub() {
        String code = GithubResponses.사용자1.code();

        TokenResponse tokenResponse = githubService.createToken(code);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(createToken(GithubResponses.사용자1.email()));
    }
}
