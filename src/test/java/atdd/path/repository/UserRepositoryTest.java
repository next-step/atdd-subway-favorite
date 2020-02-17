package atdd.path.repository;

import atdd.path.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Test
    public void createUser() {
        User user = new User("boorwonie@email.com", "브라운", "subway");

       User persistUser = userRepository.save(user);

       assertThat(persistUser.getId()).isEqualTo(1);
    }

}
