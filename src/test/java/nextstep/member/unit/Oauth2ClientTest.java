package nextstep.member.unit;

import nextstep.auth.domain.Oauth2Client;
import nextstep.member.domain.FakeGithubClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Oauth2ClientTest {

    Oauth2Client client;

    @BeforeEach
    void setUp() {
        // give
        client = new FakeGithubClientImpl();
    }

    @Test
    @DisplayName("액세스 토큰 조회 실패-잘못된 권한 증서")
    void getAccessToken_invalidCode() {
        // when
        // then
        assertThatThrownBy(() -> client.getAccessToken("code"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_AUTHENTICATION_INFO);
    }

    @ParameterizedTest
    @CsvSource({"832ovnq039hfjn,access_token_1", "mkfo0aFa03m,access_token_2", "m-a3hnfnoew92,access_token_3", "nvci383mciq0oq,access_token_4"})
    @DisplayName("액세스 토큰 조회")
    void getAccessToken(final String code,
                        final String expectedAccessToken) {
        // when
        final String accessToken = client.getAccessToken(code);

        // then
        assertThat(accessToken).isEqualTo(expectedAccessToken);
    }
}
