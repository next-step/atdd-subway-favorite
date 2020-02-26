package atdd.user.service;

import atdd.user.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private UserService userService;

    private UserDao userDao = mock(UserDao.class);

    @BeforeEach
    void setup() {
        this.userService = new UserService(userDao);
    }

    @DisplayName("findByEmail - 이메일에 해당하는 사용자가 없으면 에러")
    @Test
    void findByEmailNoResult() throws Exception {
        final String invalidEmail = "invalid!!!!!!!!";
        given(userDao.findByEmail(invalidEmail)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 email 입니다. email : [invalid!!!!!!!!]");
    }

}