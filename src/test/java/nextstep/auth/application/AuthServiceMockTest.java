package nextstep.auth.application;

import nextstep.auth.application.dto.AuthResponse;
import nextstep.common.exception.UnauthorizedException;
import nextstep.member.application.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceMockTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "test";

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;
    private UserDetail userDetail;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userDetailsService, jwtTokenProvider);
        userDetail = new UserDetail(1L, EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("login 을 통해 AuthResponse 를 응답받을 수 있다")
    void loginTest() {
        final String accessToken = "access_token";

        given(userDetailsService.loadUserByEmail(EMAIL)).willReturn(Optional.of(userDetail));
        given(jwtTokenProvider.createToken(userDetail.getId(), userDetail.getEmail())).willReturn(accessToken);

        final AuthResponse authResponse = authService.login(EMAIL, PASSWORD);

        assertThat(authResponse.getAccessToken()).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("login 시 해당 email 의 user 가 존재하지 않는다면 UnauthorizedException 이 던져진다.")
    void loadEmptyTest() {
        final String wrongEmail = "wrong@wrong.com";
        given(userDetailsService.loadUserByEmail(wrongEmail)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(wrongEmail, PASSWORD))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("login 시 비밀번호가 틀리다면 UnauthorizedException 이 던져진다.")
    void wrongPasswordTest() {
        final String wrongPassword = "wrongPassword";
        given(userDetailsService.loadUserByEmail(EMAIL)).willReturn(Optional.of(userDetail));

        assertThatThrownBy(() -> authService.login(EMAIL, wrongPassword))
                .isInstanceOf(UnauthorizedException.class);
    }
}
