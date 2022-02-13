package nextstep.subway.unit;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationInterceptor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationInterceptorTest extends AuthenticationInterceptorTestSupport {
    @Test
    void authenticate() {
        // given
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(userDetailsService);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 1));

        // when
        Authentication authentication = authenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));

        // then
        LoginMember loginMember = (LoginMember) authentication.getPrincipal();
        assertThat(loginMember.getId()).isEqualTo(1L);
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getPassword()).isEqualTo(PASSWORD);
        assertThat(loginMember.getAge()).isEqualTo(1);
    }

    @Test
    void authenticateInValidationFailure() {
        // given
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(userDetailsService);
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(new LoginMember(1L, EMAIL, PASSWORD, 1));

        // when then
        assertThrows(AuthenticationException.class, () -> authenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD + "123")));
    }

    @Test
    void authenticateNotExistUserFailure() {
        // given
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(userDetailsService);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(null);

        // when then
        assertThrows(AuthenticationException.class, () -> authenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD)));
    }
}
