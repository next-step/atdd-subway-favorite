package atdd.path.application;

import atdd.path.TestConstant;
import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.repository.UserRepository;
import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository);
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
}

