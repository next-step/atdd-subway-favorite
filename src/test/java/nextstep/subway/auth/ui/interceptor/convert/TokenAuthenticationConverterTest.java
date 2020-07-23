package nextstep.subway.auth.ui.interceptor.convert;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private TokenAuthenticationConverter converter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        converter = new TokenAuthenticationConverter();
    }

    @DisplayName("token 생성 테스트")
    @Test
    void AuthConvert() {
        // given
        addBasicAuthHeader(EMAIL, PASSWORD);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("basic auth 없을시 에러 발생")
    @Test
    void convertWithoutBasicAuth() {
        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(RuntimeException.class);
    }

    private void addBasicAuthHeader(String email, String password) {
        byte[] targetBytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }
}
