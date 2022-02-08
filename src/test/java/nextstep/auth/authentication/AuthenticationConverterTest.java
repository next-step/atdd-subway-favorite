package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("인증 컨버터(AuthenticationConverter)")
class AuthenticationConverterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("정상적인 요청값이 들어왔을 경우 인증 토큰을 받을수 있다")
    @Test
    void convert() throws IOException {
        // given
        final AuthenticationConverter authenticationConverter = mock(AuthenticationConverter.class);
        final MockHttpServletRequest mockRequest = createMockRequest();
        final AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        given(authenticationConverter.convert(mockRequest)).willReturn(authenticationToken);

        // when
        final AuthenticationToken actual = authenticationConverter.convert(mockRequest);

        // then
        assertThat(actual).isEqualTo(authenticationToken);
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
