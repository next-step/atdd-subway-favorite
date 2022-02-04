package nextstep.subway.acceptance.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenAuthenticationInterceptorTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwfSIsImlhdCI6MTY0Mzk3NjQ4MCwiZXhwIjoxNjQzOTgwMDgwfQ.JymJnBJ-Oefes2n3IKO2xZ6KRWJLCEN3ddWCrc23EnI";

    @DisplayName("HttpRequest를 AuthenticationToken으로 변환 테스트")
    @Test
    void convert() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Authentication 객체 생성 테스트")
    @Test
    void preHandle() throws IOException {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }
}