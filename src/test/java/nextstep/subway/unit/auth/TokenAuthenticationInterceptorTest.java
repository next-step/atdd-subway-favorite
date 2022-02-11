package nextstep.subway.unit.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.authentication.*;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TokenAuthenticationInterceptorTest {
    UserDetailsService userDetailsService;
    JwtTokenProvider jwtTokenProvider;
    AuthenticationConverter converter;
    AuthenticationInterceptor interceptor;
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        converter = new TokenAuthenticationConverter();
        interceptor = new TokenAuthenticationInterceptor(
                userDetailsService, converter, jwtTokenProvider, new ObjectMapper());
        request = createTokenMockRequest();
        response = createMockResponse();
    }

    @Test
    void authenticate() {
        // given
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        given(userDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));

        // when
        Authentication authentication = interceptor.authenticate(token);

        // then
        assertThat(authentication.getPrincipal()).isNotNull();
    }

    @Test
    void afterAuthentication() throws IOException {
        // given
        Authentication authentication = createAuthentication();
        given(jwtTokenProvider.createToken(anyString())).willReturn(JWT_TOKEN);

        // when
        interceptor.afterAuthentication(request, response, authentication);

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }

    @Test
    void preHandle() throws Exception {
        // given
        given(userDetailsService.loadUserByUsername(EMAIL))
                .willReturn(new LoginMember(1L, EMAIL, PASSWORD, 20));
        given(jwtTokenProvider.createToken(anyString())).willReturn(JWT_TOKEN);

        // when
        interceptor.preHandle(request, response, new Object());

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getContentAsString()).isEqualTo(new ObjectMapper().writeValueAsString(new TokenResponse(JWT_TOKEN)));
    }
}