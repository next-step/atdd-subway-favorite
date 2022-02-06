package nextstep.subway.unit.auth;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;

@ExtendWith(MockitoExtension.class)
class AuthenticationInterceptorTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @InjectMocks
    private AuthenticationInterceptorImpl authenticationInterceptor;

    @Test
    void authenticate() {
        // given
        Long ID = 1L;
        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 10;
        LoginMember loginMember = new LoginMember(1L, EMAIL, PASSWORD, 10);
        when(customUserDetailsService.loadUserByUsername(EMAIL))
            .thenReturn(loginMember);

        // when
        Authentication authentication = authenticationInterceptor.authenticate(new AuthenticationToken(EMAIL, PASSWORD));
        LoginMember returnLoginMember = (LoginMember) authentication.getPrincipal();
        // then
        assertThat(returnLoginMember.getId()).isEqualTo(ID);
        assertThat(returnLoginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(returnLoginMember.getPassword()).isEqualTo(PASSWORD);
        assertThat(returnLoginMember.getAge()).isEqualTo(AGE);
    }
}
