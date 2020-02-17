package atdd.path.repository;

import atdd.path.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void createUser() {
        User user = new User("boorwonie@email.com", "브라운", "subway");

        User persistUser = userRepository.save(user);

        assertThat(persistUser.getName()).isEqualTo("브라운");
    }
}
