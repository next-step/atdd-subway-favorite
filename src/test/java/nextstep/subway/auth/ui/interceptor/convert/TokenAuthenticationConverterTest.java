package nextstep.subway.auth.ui.interceptor.convert;

import static org.assertj.core.api.Assertions.*;

import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import nextstep.subway.auth.domain.AuthenticationToken;

public class TokenAuthenticationConverterTest {

    private static final String EMAIL = "honux77@honux.com";
    private static final String PASSWORD = "honux_loves_jujitsu";

    private TokenAuthenticationConverter converter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        converter = new TokenAuthenticationConverter();
        request = new MockHttpServletRequest();
    }

    @DisplayName("토큰을 생성할 수 있다.")
    @Test
    void 토큰을_생성한다() {
        // given
        addHeader(EMAIL, PASSWORD);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(EMAIL);
        assertThat(token.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Basic Auth가 없을 경우 오류를 반환한다.")
    @Test
    void 이메일_또는_비밀번호가_없을_경우_오류를_반환한다() {
        assertThatThrownBy(
            () -> converter.convert(request)
        ).isInstanceOf(RuntimeException.class).hasMessage("no principal or credentials found.");
    }

    private void addHeader(String email, String password) {
        byte[] targetBytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(targetBytes);
        String credentials = new String(encodedBytes);
        request.addHeader("Authorization", "Basic " + credentials);
    }
}
