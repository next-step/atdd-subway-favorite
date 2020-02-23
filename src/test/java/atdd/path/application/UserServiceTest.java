package atdd.path.application;

import atdd.path.SoftAssertionTest;
import atdd.path.application.dto.user.FindByEmailResponseView;
import atdd.path.application.dto.user.UserDetailResponseView;
import atdd.path.application.dto.user.UserLoginResponseView;
import atdd.path.application.dto.user.UserSighUpResponseView;
import atdd.path.application.exception.ExistUserException;
import atdd.path.dao.UserDao;
import atdd.path.security.TokenAuthenticationService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.fixture.UserFixture.*;
import static atdd.path.security.TokenAuthenticationService.BEARER_TOKEN_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest extends SoftAssertionTest {
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private TokenAuthenticationService tokenAuthenticationService;

    @BeforeEach
    void setUp() {
        this.tokenAuthenticationService = new TokenAuthenticationService();
        this.userService = new UserService(tokenAuthenticationService, userDao);
    }

    @DisplayName("이미 회원이 있는 유저인 경우 예외를 처리하는지")
    @Test
    public void sighUpWhenExistUser() {
        when(userDao.findByEmail(any())).thenReturn(FIND_BY_EMAIL_RESPONSE_VIEW);

        Assertions.assertThrows(ExistUserException.class, () -> {
            userService.singUp(USER_SIGH_UP_REQUEST_DTO);
        });
    }

    @DisplayName("회원이 정상적으로 등록되는지")
    @Test
    public void signUp() {
        when(userDao.findByEmail(any())).thenReturn(FindByEmailResponseView.builder().build());
        when(userDao.save(any())).thenReturn(NEW_USER);

        UserSighUpResponseView user = userService.singUp(USER_SIGH_UP_REQUEST_DTO);

        assertThat(user.getId()).isNotNull();
    }

    @DisplayName("회원이 로그인이 성공하여 토큰을 리턴하는지")
    @Test
    public void login(SoftAssertions softly) {
        when(userDao.findByEmail(any())).thenReturn(FIND_BY_EMAIL_RESPONSE_VIEW);

        UserLoginResponseView user = userService.login(USER_LOGIN_REQUEST_DTO);

        softly.assertThat(user.getAccessToken()).isNotNull();
        softly.assertThat(user.getTokenType()).isEqualTo(BEARER_TOKEN_TYPE);
    }

    @DisplayName("회원의 ID 로 상세정보가 되는지")
    @Test
    public void findById(SoftAssertions softly) {
        when(userDao.findById(any())).thenReturn(NEW_USER);

        UserDetailResponseView user = userService.findById(KIM_ID);

        softly.assertThat(user.getEmail()).isEqualTo(KIM_EMAIL);
        softly.assertThat(user.getName()).isEqualTo(KIM_NAME);
    }

}
