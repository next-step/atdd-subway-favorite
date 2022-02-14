package nextstep.subway.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.SesstionAuthenticationConverter;
import nextstep.auth.authentication.TokenAuthenticationConverter;
import nextstep.auth.token.ObjectMapperBean;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("컨버터 관리 - 세션")
@ExtendWith(MockitoExtension.class)
class SessionAuthenticationConverterTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    @Mock
    private SesstionAuthenticationConverter converter;

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletRequest mockRequest = createMockRequest();
        when(converter.convert(any())).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        // when
        AuthenticationToken authenticationToken = converter.convert(mockRequest);

        // then
        assertAll(
                () -> assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}
