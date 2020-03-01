package atdd.user.dao;

import atdd.user.domain.User;
import atdd.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void findByEmail() {
        //given
        String email = "me@email.com";
        User user1 = new User(email, "namename", "pwdwewd");
        userRepository.save(user1);

        //when
        User user2 = userRepository.findByEmail(email);

        //then
        assertEquals(email, user2.getEmail());
    }
}
