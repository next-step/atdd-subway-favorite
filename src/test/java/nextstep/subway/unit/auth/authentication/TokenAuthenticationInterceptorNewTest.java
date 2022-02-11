package nextstep.subway.unit.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convert.SessionAuthenticationConverter;
import nextstep.auth.authentication.interceptor.BaseAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.SessionAuthenticationInterceptor;
import nextstep.auth.authentication.interceptor.TokenAuthenticationInterceptor;
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

class TokenAuthenticationInterceptorNewTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJpZFwiOjEsXCJlbWFpbFwiOlwiZW1haWxAZW1haWwuY29tXCIsXCJwYXNzd29yZFwiOlwicGFzc3dvcmRcIixcImFnZVwiOjIwfSIsImlhdCI6MTY0Mzk3NjQ4MCwiZXhwIjoxNjQzOTgwMDgwfQ.JymJnBJ-Oefes2n3IKO2xZ6KRWJLCEN3ddWCrc23EnI";

    @DisplayName("HttpRequest를 AuthenticationToken으로 변환 테스트 - token")
    @Test
    void convertToken() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);

        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        BaseAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);
        MockHttpServletRequest request = createMockRequest();

        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("HttpRequest를 AuthenticationToken으로 변환 테스트 - session")
    @Test
    void convertSession() throws IOException {
        // given
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);

        BaseAuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(userDetailsService);
        MockHttpServletRequest request = createMockSessionRequest();

        // when
        AuthenticationToken authenticationToken = interceptor.convert(request);

        // then
        assertThat(authenticationToken.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authenticationToken.getCredentials()).isEqualTo(PASSWORD);
    }

    @DisplayName("Authentication 객체 생성 테스트 - token")
    @Test
    void preHandleToken() throws IOException {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        TokenAuthenticationInterceptor interceptor = new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD));
        when(jwtTokenProvider.createToken(anyString())).thenReturn(JWT_TOKEN);

        MockHttpServletRequest request = createMockRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @DisplayName("Authentication 객체 생성 테스트 - session")
    @Test
    void preHandleSession() throws IOException {
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        BaseAuthenticationInterceptor interceptor = new SessionAuthenticationInterceptor(userDetailsService);

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD));

        MockHttpServletRequest request = createMockSessionRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.preHandle(request, response, new Object());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

    private MockHttpServletRequest createMockSessionRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(SessionAuthenticationConverter.USERNAME_FIELD, EMAIL);
        request.setParameter(SessionAuthenticationConverter.PASSWORD_FIELD, PASSWORD);
        return request;
    }
}