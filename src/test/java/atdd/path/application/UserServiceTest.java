package atdd.path.application;

import atdd.path.application.dto.UserRequestView;
import atdd.path.application.dto.UserResponseView;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userDao);
    }

    @DisplayName("회원 등록이 된다")
    @Test
    void createUserTest() {
        // given
        UserRequestView userInfo = UserRequestView.builder()
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();
        given(userDao.save(any(User.class)))
                .willReturn(User.createBuilder().email("boorwonie@email.com").name("브라운").build());

        // when
        UserResponseView response = userService.createUser(userInfo);

        // then
        assertThat(response.getName()).isEqualTo("브라운");
    }

    @DisplayName("회원 탈퇴가 된다")
    @Test
    void deleteUserTest() {
    }
}

