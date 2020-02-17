package atdd.path.repository;

import atdd.path.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DataJpaTest
public class UserRepositoryTest {
    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUser() {
        User user = new User("boorwonie@email.com", "브라운", "subway");
        given(userRepository.save(user)).willReturn(new User("boorwonie@email.com", "브라운", "subway"));

        User persistUser = userRepository.save(user);

        assertThat(persistUser.getName()).isEqualTo("브라운");
    }
}
