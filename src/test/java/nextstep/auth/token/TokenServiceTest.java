package nextstep.auth.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("토큰 서비스 테스트")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @DisplayName("깃 허브 토큰 생성 테스트")
    @ParameterizedTest
    @ValueSource(strings = {
            "aofijeowifjaoief",
            "fau3nfin93dmn",
            "afnm93fmdodf",
            "fm04fndkaladmd"
    })
    void createTokenFromGithub(String code) {
        // given : 선행조건 기술

        // when : 기능 수행
        TokenResponse tokenResponse = tokenService.createTokenFromGithub(code);

        // then : 결과 확인
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }
}
