package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenResponse;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.member.acceptance.AuthSteps.createToken;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubServiceTest {
    @Autowired
    private GithubService githubService;

    @DisplayName("code를 통한 Github Login")
    @Test
    void createTokenByGithub() {
        String code = GithubResponses.사용자1.code();

        TokenResponse tokenResponse = githubService.createToken(code);

        assertThat(tokenResponse.getAccessToken()).isEqualTo(createToken(GithubResponses.사용자1.email()));
    }
}
