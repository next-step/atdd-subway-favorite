package atdd.user.service;

import atdd.security.LoginUserInfo;
import atdd.security.LoginUserRegistry;
import atdd.user.dto.AccessToken;
import atdd.user.dto.TokenType;
import atdd.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    private UserService userService = mock(UserService.class);
    private JsonWebTokenProvider jsonWebTokenProvider = mock(JsonWebTokenProvider.class);
    private LoginUserRegistry loginUserRegistry = mock(LoginUserRegistry.class);

    private final String email = "email@email.com";
    private final String password = "password!!";
    private final String name = "name!!";

    private final String authorizationType = "Bearer";
    private final String token = "token!!!";
    private final String authorization = authorizationType + " " + token;
    private final LoginUserInfo loginUser = LoginUserInfo.of(13L, email, name, password);
    private final Date nowDate = new Date();

    @BeforeEach
    void setup() {
        this.authorizationService = new AuthorizationService(userService, jsonWebTokenProvider, loginUserRegistry);
    }

    @Test
    void authorize() throws Exception {
        final UserResponseDto userResponseDto = UserResponseDto.of(1L, email, name, password);
        final String expectedToken = "token!!!";

        given(userService.findByEmail(email)).willReturn(userResponseDto);
        given(jsonWebTokenProvider.createToken(eq(email), any(Date.class))).willReturn(expectedToken);


        final AccessToken accessToken = authorizationService.authorize(email, password);


        assertThat(accessToken.getAccessToken()).isEqualTo(expectedToken);
        assertThat(accessToken.getTokenType()).isEqualTo(TokenType.BEARER.getTypeName());

        verify(userService, times(1)).findByEmail(email);
        verify(jsonWebTokenProvider, times(1)).createToken(eq(email), any(Date.class));
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

    @Test
    void isAuthorized() throws Exception {

        given(jsonWebTokenProvider.isExpiredToken(token, nowDate)).willReturn(false);
        given(jsonWebTokenProvider.parseEmail(token)).willReturn(email);
        given(userService.findLoginUser(email)).willReturn(Optional.of(loginUser));

        final boolean authorized = authorizationService.isAuthorized(authorization, nowDate);


        assertThat(authorized).isTrue();
        verify(loginUserRegistry, times(1)).setCurrentLoginUser(loginUser);
    }

    @DisplayName("isAuthorized - authorization 값이 없으면 false")
    @ParameterizedTest
    @NullAndEmptySource
    void isAuthorizedEmptyAuthorization(String emtpyAuthorization) throws Exception {
        final boolean authorized = authorizationService.isAuthorized(emtpyAuthorization, nowDate);

        assertThat(authorized).isFalse();
    }

    @DisplayName("isAuthorized - 현재 시간 값이 없으면 false")
    @Test
    void isAuthorizedEmptyAuthorization() throws Exception {
        final Date nullDate = null;

        final boolean authorized = authorizationService.isAuthorized(authorization, nullDate);

        assertThat(authorized).isFalse();
    }

    @DisplayName("isAuthorized - authorization 의 타입과 토큰 사이에 공백이 없으면 false")
    @Test
    void isAuthorizedInvalidLength() throws Exception {
        final String tightAuthorization = "Bearertoken!!!";

        final boolean authorized = authorizationService.isAuthorized(tightAuthorization, nowDate);

        assertThat(authorized).isFalse();
    }

    @DisplayName("isAuthorized - authorization 의 타입이 Bearer 가 아니면 false")
    @Test
    void isAuthorizedNotBearer() throws Exception {
        final String tightAuthorization = "Basic token!!!";

        final boolean authorized = authorizationService.isAuthorized(tightAuthorization, nowDate);

        assertThat(authorized).isFalse();
    }

    @DisplayName("isAuthorized - authorization 의 토큰이 만료되었으면 false")
    @Test
    void isAuthorizedExpired() throws Exception {
        given(jsonWebTokenProvider.isExpiredToken(token, nowDate)).willReturn(true);

        final boolean authorized = authorizationService.isAuthorized(authorization, nowDate);

        assertThat(authorized).isFalse();
    }

    @DisplayName("isAuthorized - 토큰의 email 을 찾을 수 없으면 false")
    @Test
    void isAuthorizedNotFoundEmail() throws Exception {

        given(jsonWebTokenProvider.isExpiredToken(token, nowDate)).willReturn(false);
        given(jsonWebTokenProvider.parseEmail(token)).willReturn(email);
        given(userService.findLoginUser(email)).willReturn(Optional.empty());

        final boolean authorized = authorizationService.isAuthorized(authorization, nowDate);


        assertThat(authorized).isFalse();
        verify(loginUserRegistry, times(0)).setCurrentLoginUser(any());
    }

}