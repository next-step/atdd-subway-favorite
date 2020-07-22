package nextstep.subway.auth.application;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.member.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationProviderTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "dhlee@test.com";
    private static final String PASSWORD = "Pas5W0rb12$%";
    private static final Integer AGE = 10;


    @Test
    @DisplayName("인증정보를 제공하기 위한 클래스")
    public void authenticateTest() {
        UserDetailsService userDetailService = mock(UserDetailsService.class);
        when(userDetailService.loadUserByUsername(anyString())).thenReturn(new LoginMember(ID, EMAIL, PASSWORD, AGE));
        // given
        AuthenticationProvider authenticationProvider = new AuthenticationProvider(userDetailService);
        AuthenticationToken authenticationToken = new AuthenticationToken(EMAIL, PASSWORD);

        // when
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);

        // then
        assertThat(authentication).isNotNull();
        Object principal = authentication.getPrincipal();
        assertThat(principal).isInstanceOf(LoginMember.class);

        LoginMember loginMember = (LoginMember) principal;
        assertThat(loginMember.getId()).isEqualTo(ID);
        assertThat(loginMember.getEmail()).isEqualTo(EMAIL);
        assertThat(loginMember.getAge()).isEqualTo(AGE);

    }
}