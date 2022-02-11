package nextstep.subway.unit;

import nextstep.auth.application.TokenAuthenticationConverter;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static nextstep.subway.unit.AuthenticationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("토큰 기반 인증")
class TokenAuthenticationInterceptorTest {

    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationConverter converter;
    private AuthenticationInterceptor interceptor;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        converter = mock(TokenAuthenticationConverter.class);
        interceptor = new TokenAuthenticationInterceptor(userDetailsService, converter, jwtTokenProvider, FIXTURE_OBJECT_MAPPER);
    }

    @Test
    void preHandle() throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 20);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(any())).thenReturn(JWT_TOKEN);
        when(converter.convert(any())).thenReturn(new AuthenticationToken(EMAIL, PASSWORD));

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        interceptor.preHandle(createMockRequest(), response, new Object());

        int status = response.getStatus();
        String contentType = response.getContentType();
        String content = response.getContentAsString();

        //then
        assertAll(
                () -> assertThat(status).isEqualTo(HttpStatus.SC_OK),
                () -> assertThat(contentType).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
                () -> assertThat(content).isEqualTo(FIXTURE_OBJECT_MAPPER.writeValueAsString(new TokenResponse(JWT_TOKEN)))
        );
    }

}
