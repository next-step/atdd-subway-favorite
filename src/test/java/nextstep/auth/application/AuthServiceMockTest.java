package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.member.application.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceMockTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("login 을 통해 AuthResponse 를 응답받을 수 있다")
    void loginTest() {
        final UserDetail userDetail = new UserDetail();

        final String accessToken = "access_token";

        given(userDetailsService.loadUserByEmail("email@email.com")).willReturn(userDetail);
        given(jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail())).willReturn(accessToken);

        final AuthService authService = new AuthService(userDetailsService, jwtTokenProvider);
        final AuthResponse authResponse = authService.login("email@email.com", "test");

        assertThat(authResponse.getAccessToken()).isEqualTo(accessToken);
    }
}
