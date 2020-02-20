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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {UserService.class, JwtTokenProvider.class})
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, jwtTokenProvider);
    }

    @DisplayName("회원 등록이 된다")
    @Test
    void createUser() {
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
    void deleteUser() {
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
        verify(userRepository).deleteById(any());
    }

    @DisplayName("회원 정보를 가져온다")
    @Test
    void retrieveUser() throws InvalidJwtAuthenticationException {
        // given
        given(userRepository.findUserByEmail(TestConstant.EMAIL_BROWN))
                .willReturn(User.createBuilder()
                        .id(1L)
                        .email(TestConstant.EMAIL_BROWN)
                        .name(TestConstant.NAME_BROWN)
                        .password(TestConstant.PASSWORD_BROWN)
                        .build());
        LoginResponseView token = userService.login(LoginRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN).build());

        // when
        UserResponseView info = userService.retrieveUser(String.format("%s %s", token.getTokenType(), token.getAccessToken()));

        // then
        assertThat(info.getEmail()).isEqualTo(TestConstant.EMAIL_BROWN);
        assertThat(info.getName()).isEqualTo(TestConstant.NAME_BROWN);
    }

    @DisplayName("사용자는 자신의 정보를 통해 로그인을 요청하고 인증 정보를 받는다")
    @Test
    void login() {
        // given
        User brown = User.createBuilder()
                .id(1L)
                .name(TestConstant.NAME_BROWN)
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();

        given(userRepository.save(any(User.class)))
                .willReturn(brown);
        given(userRepository.findUserByEmail(TestConstant.EMAIL_BROWN)).willReturn(brown);
        userService.createUser(new UserRequestView());

        // when
        LoginRequestView loginRequest = LoginRequestView.builder()
                .email(TestConstant.EMAIL_BROWN)
                .password(TestConstant.PASSWORD_BROWN)
                .build();
        LoginResponseView response = userService.login(loginRequest);

        // then
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getAccessToken()).isNotNull();
    }
}

