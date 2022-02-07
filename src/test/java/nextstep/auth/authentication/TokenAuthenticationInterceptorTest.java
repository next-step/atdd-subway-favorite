package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.auth.authentication.step.AuthenticationStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationInterceptorTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private Authentication authentication;
    private CustomUserDetailsService customUserDetailsService;
    private TokenAuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new TokenAuthenticationInterceptor(customUserDetailsService, new TokenAuthenticationConverter(), jwtTokenProvider);
        authentication = new Authentication();
    }

    @DisplayName("인증 정보를확인 하고 응답한다")
    @Test
    void afterAuthentication() throws IOException {
        // given
        MockHttpServletResponse 응답 = 인증_응답_mock();
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        // when
        interceptor.afterAuthentication(token_인증_요청_mock(), 응답, authentication);

        // then
        assertThat(응답.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(응답.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(응답.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

}