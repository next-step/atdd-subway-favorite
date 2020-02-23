package atdd.user.service;

import atdd.user.dto.AccessToken;
import atdd.user.dto.TokenType;
import atdd.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    private UserService userService = mock(UserService.class);
    private JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

    private final String email = "email@email.com";
    private final String password = "password!!";
    private final String name = "name!!";

    @BeforeEach
    void setup() {
        this.authorizationService = new AuthorizationService(userService, jwtTokenProvider);
    }

    @Test
    void authorize() throws Exception {
        final UserResponseDto userResponseDto = UserResponseDto.of(1L, email, name, password);
        final String expectedToken = "token!!!";

        given(userService.findByEmail(email)).willReturn(userResponseDto);
        given(jwtTokenProvider.createToken(email)).willReturn(expectedToken);


        final AccessToken accessToken = authorizationService.authorize(email, password);


        assertThat(accessToken.getAccessToken()).isEqualTo(expectedToken);
        assertThat(accessToken.getTokenType()).isEqualTo(TokenType.BEARER.getTypeName());

        verify(userService, times(1)).findByEmail(email);
        verify(jwtTokenProvider, times(1)).createToken(email);
    }

    @DisplayName("authorize - email 은 필수값")
    @ParameterizedTest
    @NullAndEmptySource
    void authorizeEmptyEmail(String email) throws Exception {

        assertThatThrownBy(() -> authorizationService.authorize(email, "password!!"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email 은 필수입니다.");

    }

    @DisplayName("authorize - password 는 필수값")
    @ParameterizedTest
    @NullAndEmptySource
    void authorizeEmptyPassword(String password) throws Exception {

        assertThatThrownBy(() -> authorizationService.authorize("email@email.com", password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password 는 필수입니다.");

    }

    @DisplayName("authorize - 비밀번호가 일치하지 않으면 에러")
    @Test
    void authorizeNotMatch() throws Exception {
        final String notMatchPassword = "no!!!!";

        given(userService.findByEmail(email)).willReturn(UserResponseDto.of(1L, email, name, password));

        assertThatThrownBy(() -> authorizationService.authorize(email, notMatchPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password 가 일치하지 않습니다. password : [no!!!!]");
    }

}