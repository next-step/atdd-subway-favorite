package atdd.path.application;

import atdd.path.application.exception.ExistUserException;
import atdd.path.application.exception.NoDataException;
import atdd.path.dao.UserDao;
import atdd.path.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @DisplayName("이미 회원이 있는 유저인 경우 예외를 처리하는지")
    @Test
    public void sighUpWhenExistUser() {
        when(userDao.findByEmail(any())).thenReturn(getDaoUsers());

        Assertions.assertThrows(ExistUserException.class, () -> {
            userService.singUp(USER_SIGH_UP_REQUEST_DTO);
        });
    }

    @DisplayName("회원이 정상적으로 등록되는지")
    @Test
    public void signUp() {
        when(userDao.findByEmail(any())).thenReturn(null);
        when(userDao.save(any())).thenReturn(NEW_USER);

        User user = userService.singUp(USER_SIGH_UP_REQUEST_DTO);

        assertThat(user.getId()).isNotNull();
    }
}
