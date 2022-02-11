package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.subway.unit.AuthenticationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest {

    private CustomUserDetailsService userDetailsService;
    private AuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(CustomUserDetailsService.class);
        converter = mock(AuthenticationConverter.class);
    }

    @Test
    void authenticate() {
        //given
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(userDetailsService, converter) {
            @Override
            public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

            }

        };
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(FIXTURE_LOGIN_MEMBER);

        //when
        Authentication authentication = interceptor.authenticate(token);

        //then
        assertThat(authentication.getPrincipal()).isNotNull();

    }

}
