package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static nextstep.auth.util.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("인증 컨버터(AuthenticationConverter)")
class AuthenticationConverterTest {

    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // given
        final HttpServletRequest request = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationConverter authenticationConverter = mock(AuthenticationConverter.class);

        given(authenticationConverter.convert(request)).willReturn(new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD));

        // when
        final AuthenticationToken actual = authenticationConverter.convert(request);

        // then
        assertThat(actual).isNotNull();
    }
}
