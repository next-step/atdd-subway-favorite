package atdd.path.repository;

import atdd.path.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void readUsers() {
        createUser();

        List<User> userList = new ArrayList<User>();

        userRepository.findAll().forEach(userList::add);

        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0).getName()).isEqualTo("브라운");
    }
}
