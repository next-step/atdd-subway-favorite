package atdd.path.application;

import atdd.path.dao.UserRepository;
import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser() {
        //given
        User user = User.builder()
                .id(1L)
                .build();

        given(userRepository.save(user)).willReturn(user);

        //when
        User createdUser = userService.create(user);

        //then
        assertThat(createdUser).isEqualTo(user);
    }
}
