package atdd.path.application;

import atdd.path.TestConstant;
import atdd.path.application.dto.LoginRequestView;
import atdd.path.application.dto.LoginResponseView;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.domain.User;
import atdd.path.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {UserService.class, LoginService.class, JwtTokenProvider.class})
public class UserServiceTest {

    private UserService userService;

    private LoginService loginService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, jwtTokenProvider);
        this.loginService = new LoginService(jwtTokenProvider);
    }

    @DisplayName("회원 등록이 된다")
    @Test
    void createUserTest() {
        // given
        UserRequestView userInfo = UserRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .name(TestConstant.NAME_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();
        given(userRepository.save(any(User.class)))
                .willReturn(User.createBuilder().email(TestConstant.EMAIL_BROWN).name(TestConstant.NAME_BROWN).build());

        // when
        UserResponseView response = userService.createUser(userInfo);

        // then
        assertThat(response.getName()).isEqualTo(TestConstant.NAME_BROWN);
    }

    @DisplayName("회원 탈퇴가 된다")
    @Test
    void deleteUserTest() {
        // give
        UserRequestView userInfo = UserRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .name(TestConstant.NAME_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();
        given(userRepository.save(any(User.class)))
                .willReturn(User.createBuilder().id(1L).email(TestConstant.EMAIL_BROWN).name(TestConstant.NAME_BROWN).build());
        UserResponseView response = userService.createUser(userInfo);

        // when
        userService.deleteUser(response.getId());

        // then
        assertThat(userRepository.findById(response.getId()).orElse(null)).isNull();
    }

    @DisplayName("회원 정보를 가져온다")
    @Test
    void retrieveUserTest() throws InvalidJwtAuthenticationException {
        // given
        given(userRepository.findUserByEmail(TestConstant.EMAIL_BROWN))
                .willReturn(User.infoBuilder().id(1L).email(TestConstant.EMAIL_BROWN).name(TestConstant.NAME_BROWN).build());
        LoginResponseView token = loginService.login(LoginRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN).build());

        // when
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", String.format("%s %s", token.getTokenType(), token.getAccessToken()));
        UserResponseView info = userService.retrieveUser(req);

        // then
        assertThat(info.getEmail()).isEqualTo(TestConstant.EMAIL_BROWN);
        assertThat(info.getName()).isEqualTo(TestConstant.NAME_BROWN);
    }
}

