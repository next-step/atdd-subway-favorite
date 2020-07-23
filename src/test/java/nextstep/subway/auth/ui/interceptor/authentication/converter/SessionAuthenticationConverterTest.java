package nextstep.subway.auth.ui.interceptor.authentication.converter;

import nextstep.subway.auth.domain.AuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessionAuthenticationConverterTest {

    private SessionAuthenticationConverter converter;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        converter = new SessionAuthenticationConverter();
        request = new MockHttpServletRequest();
    }

    @DisplayName("username 파라미터가 없을시 NullPointerException")
    @Test
    void convert_withoutUsername() {
        // given
        request.addParameter("password", "password1234");

        // when
        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("password 파라미터가 없을시 NullPointerException")
    @Test
    void convert_withoutPassword() {
        // given
        request.addParameter("username", "username1234");

        // when
        assertThatThrownBy(() -> converter.convert(request))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("입력한 username, password를 바탕으로 AuthenticationToken을 생성한다")
    @Test
    void convert() {
        // given
        String username = "username1234";
        String password = "password1234";
        request.addParameter("username", username);
        request.addParameter("password", password);

        // when
        AuthenticationToken token = converter.convert(request);

        // then
        assertThat(token.getPrincipal()).isEqualTo(username);
        assertThat(token.getCredentials()).isEqualTo(password);
    }
}
