package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.unit.AuthenticationFixture.EMAIL;
import static nextstep.subway.unit.AuthenticationFixture.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
            public void afterAuthentication(Authentication authentication) {

            }
        };
        AuthenticationToken token = new AuthenticationToken(EMAIL, PASSWORD);

        //when
        Authentication authentication = interceptor.authenticate(token);

        //then
        assertThat(authentication.getPrincipal()).isNotNull();

    }

}
