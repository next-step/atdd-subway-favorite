package atdd.path.application;

import atdd.path.TestConstant;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = LoginService.class)
public class LoginServiceTest {

    private LoginService loginService;

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.loginService = new LoginService();
        this.userService = new UserService(userRepository);
    }

    @DisplayName("사용자는 자신의 정보를 통해 로그인을 요청하고 인증 정보를 받는다")
    @Test
    void loginTest() {
        // given
        given(userRepository.save(any(User.class)))
                .willReturn(User.createBuilder().id(1L).name(TestConstant.NAME_BROWN).email(TestConstant.EMAIL_BROWN).build());
        UserResponseView savedUser = userService.createUser(new UserRequestView());

        // when
        LoginRequestView loginRequest = LoginRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();
        LoginResponseView response = loginService.login(loginRequest);

        // then
        assertThat(response.getTokenType()).isEqualTo("bearer");
        assertThat(response.getAccessToken()).isNotNull();
    }
}
