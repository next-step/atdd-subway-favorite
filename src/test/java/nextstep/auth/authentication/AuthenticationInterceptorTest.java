package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.authentication.step.AuthenticationStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("인증 검증 interceptor")
@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
    }

    @DisplayName("토큰을 이용한 인증")
    @Test
    void preHandle_token() throws IOException {
        // given
        MockHttpServletRequest 요청 = token_인증_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, new TokenAuthenticationConverter(), jwtTokenProvider);

        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        boolean result = interceptor.preHandle(요청, 응답, new Object());

        // then
        assertThat(result).isFalse();
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(응답.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(응답.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @DisplayName("Session을 이용한 인증")
    @Test
    void preHandle() {
        // given
        MockHttpServletRequest 요청 = session_인증_요청_mock();
        MockHttpServletResponse 응답 = 인증_응답_mock();
        SessionAuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(customUserDetailsService, new SessionAuthenticationConverter());

        when(customUserDetailsService.loadUserByUsername(anyString()))
                .thenReturn(loginMember);

        // when
        boolean result = interceptor.preHandle(요청, 응답, new Object());

        // then
        assertThat(result).isFalse();
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}