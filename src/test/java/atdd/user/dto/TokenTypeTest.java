package atdd.user.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class TokenTypeTest {

    @DisplayName("isEqualTypeName - 대소문자를 구분하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"BEARER", "bearer", "Bearer"})
    void isEqualTypeName(String type) throws Exception {
        assertThat(TokenType.BEARER.isEqualTypeName(type)).isTrue();
    }

}