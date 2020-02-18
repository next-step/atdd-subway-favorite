package atdd.path.application;

import atdd.path.application.exception.NoDataException;
import atdd.path.dao.UserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static atdd.path.fixture.UserFixture.USER_SIGH_UP_REQUEST_DTO;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("이미 회원이 있는 유저인 경우 예외를 처리하는지")
    @Test
    public void sighUpWhenExistUser() {
        Long existUserId = 0L;
        Assertions.assertThrows(NoDataException.class, () -> {
            userService.singUp(USER_SIGH_UP_REQUEST_DTO);
        });
    }
}
