package nextstep.subway.unit.auth.after;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.after.TokenAfterAuthentication;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
public class TokenAfterAuthenticationTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    @Mock
    private static JwtTokenProvider jwtTokenProviderStub;
    @Mock
    private static ObjectMapper objectMapper;
    @InjectMocks
    private static TokenAfterAuthentication tokenAfterAuthentication;

    @Test
    void convert() throws IOException {
        // given
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = new Authentication(new AuthenticationToken(EMAIL, PASSWORD));
        when(jwtTokenProviderStub.createToken(any()))
            .thenReturn(JWT_TOKEN);

        // when
        tokenAfterAuthentication.afterAuthentication(new MockHttpServletRequest(), response, authentication);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isNotBlank();
    }
}
