package nextstep.member.application;

import nextstep.member.application.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("createTokenForGithub 를 통해 accessToken 을 발급 받을 수 있다.")
    void createTokenForGithubTest() {
        final TokenResponse code = tokenService.createTokenForGithub("code");

        assertThat(code.getAccessToken()).isNotBlank();
    }
}
