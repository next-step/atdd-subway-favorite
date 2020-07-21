package nextstep.subway.auth.ui.interceptor.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.converter.AuthenticationConverter;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("토큰 인증 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password1234";

    private TokenAuthenticationInterceptor interceptor;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationConverter authenticationConverter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(jwtTokenProvider, userDetailsService, authenticationConverter);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        objectMapper = new ObjectMapper();
    }

    @DisplayName("패스워드가 일치하지 않으면 RuntimeException")
    @Test
    void authenticate_notEqualPassword() {
        // given
        addAuthorizationHeader(request, EMAIL, PASSWORD);
        when(authenticationConverter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, "notequalpassword", 10));

        // when
        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("TokenResponse를 응답한다")
    @Test
    void response() throws Exception {
        // given
        String expectedToken = "token";
        addAuthorizationHeader(request, EMAIL, PASSWORD);
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(authenticationConverter.convert(request)).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(expectedToken);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(new TokenResponse(expectedToken)));
    }

    private void addAuthorizationHeader(MockHttpServletRequest request, String email, String password) {
        byte[] bytes = (email + ":" + password).getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        String authorization = new String(encodedBytes);
        request.addHeader("Authorization", "BASIC " + authorization);
    }
}
