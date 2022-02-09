package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.util.authFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("인증 컨버터(AuthenticationConverter)")
class AuthenticationConverterTest {

    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // given
        final AuthenticationConverter authenticationConverter = mock(AuthenticationConverter.class);
        final MockHttpServletRequest mockRequest = createMockRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        final AuthenticationToken authenticationToken = new AuthenticationToken(DEFAULT_EMAIL, DEFAULT_PASSWORD);

        given(authenticationConverter.convert(mockRequest)).willReturn(authenticationToken);

        // when
        final AuthenticationToken actual = authenticationConverter.convert(mockRequest);

        // then
        assertThat(actual).isEqualTo(authenticationToken);
    }
}
