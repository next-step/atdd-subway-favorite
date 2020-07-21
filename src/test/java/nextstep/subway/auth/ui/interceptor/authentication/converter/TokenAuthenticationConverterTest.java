package nextstep.subway.auth.ui.interceptor.authentication.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenAuthenticationConverterTest {

    private TokenAuthenticationConverter converter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        converter = new TokenAuthenticationConverter();
        request = new MockHttpServletRequest();
    }

    @DisplayName("BASIC 인증 스킴이 없으면 RuntimeException")
    @Test
    void convert_withoutBasicScheme() {
        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("입력한 BASIC 스킴을 바탕으로 AuthenticationToken을 생성한다")
    @Test
    void convert() {
        // given
        String username = "username1234";
        String password = "password1234";
        addAuthorizationHeader(request, username, password);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(username);
        assertThat(token.getCredentials()).isEqualTo(password);
    }

    private void addAuthorizationHeader(MockHttpServletRequest request, String email, String password) {
        byte[] bytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        String authorization = new String(encodedBytes);
        request.addHeader("Authorization", "BASIC " + authorization);
    }
}
