package nextstep.subway.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.token.TokenAuthenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenAuthenticatorTest {

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private TokenAuthenticator tokenAuthenticator;
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup(){
        jwtTokenProvider = mock(JwtTokenProvider.class);
        tokenAuthenticator = new TokenAuthenticator(jwtTokenProvider, objectMapper);
    }

    @Test
    public void authenticate() throws Exception{
        //Given
        MockHttpServletResponse response = new MockHttpServletResponse();
        Authentication authentication = mock(Authentication.class);

        //When
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);
        tokenAuthenticator.authenticate(authentication, response);

        //Then
        assertAll(
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(JWT_TOKEN)))
        );
    }

}
