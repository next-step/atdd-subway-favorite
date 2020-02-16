package atdd.path.dao;

import atdd.path.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    public User save(User user) {

        // JDBC 잘 사용할 줄 몰라요... ㅠㅠ
        User newUser = User.builder()
                .id(1L)
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .build();

        return newUser;
    }
}
