package atdd.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenInfoTest {

    @DisplayName("setSecretKey - Base64 로 변환된다.")
    @Test
    void setSecretKey() throws Exception {
        final String secretKey = "secret!!!!!!!!!";
        final String expectedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        final JwtTokenInfo jwtTokenInfo = new JwtTokenInfo();
        jwtTokenInfo.setSecretKey(secretKey);

        assertThat(jwtTokenInfo.getSecretKey()).isEqualTo(expectedSecretKey);
    }

}