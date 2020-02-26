package atdd.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonWebTokenInfoTest {

    @DisplayName("setSecretKey - Base64 로 변환된다.")
    @Test
    void setSecretKey() throws Exception {
        final String secretKey = "secret!!!!!!!!!";
        final String expectedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        final JsonWebTokenInfo jsonWebTokenInfo = new JsonWebTokenInfo();
        jsonWebTokenInfo.setSecretKey(secretKey);

        assertThat(jsonWebTokenInfo.getSecretKey()).isEqualTo(expectedSecretKey);
    }

    @DisplayName("setSecretKey - secretKey 는 필수")
    @ParameterizedTest
    @NullAndEmptySource
    void setSecretKey(String secretKey) throws Exception {
        final JsonWebTokenInfo jsonWebTokenInfo = new JsonWebTokenInfo();

        assertThatThrownBy(() -> jsonWebTokenInfo.setSecretKey(secretKey))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("secretKey 가 없습니다.");
    }

    @DisplayName("setExpireLength - expiredLength 는 1초(1000ms) 이상 이어야 한다.")
    @Test
    void setExpiredLength() throws Exception {
        final JsonWebTokenInfo jsonWebTokenInfo = new JsonWebTokenInfo();

        assertThatThrownBy(() -> jsonWebTokenInfo.setExpireLength(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("expireLength 는 1초(1000ms) 이상이어야 합니다.");
    }

}