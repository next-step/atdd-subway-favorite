package nextstep.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.AuthenticationMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private CustomUserDetailsService customUserDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor interceptor;
    private TokenAuthenticationConverter tokenAuthenticationConverter = new TokenAuthenticationConverter();

    @BeforeEach
    void setUp() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, tokenAuthenticationConverter, jwtTokenProvider);
    }

    @Test
    @DisplayName("HttpRequest 를 AuthenticationToken 으로 변환")
    void convert() throws IOException {
        MockHttpServletRequest request = createMockRequest();
        AuthenticationToken token = tokenAuthenticationConverter.convert(request);
        assertAll(
                () -> assertThat(token.getPrincipal()).isEqualTo(EMAIL),
                () -> assertThat(token.getCredentials()).isEqualTo(PASSWORD)
        );
    }

    private MockHttpServletRequest createMockRequest() throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        return request;
    }

    @Test
    @DisplayName("/login/token 으로 요청시 메시지를 파싱해 유저를 찾고 토큰을 SecurityContextHolder 에 저장 한다.")
    void preHandleTest() throws Exception {
        String token = "token";

        when(customUserDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(new AuthenticationMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString()))
                .thenReturn(token);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(token)))
        );
    }
}