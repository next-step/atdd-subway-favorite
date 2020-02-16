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

    public User findById(Long id) {

        // JDBC 잘 사용할 줄 몰라요..;;;
        User findUser = User.builder()
                .id(id)
                .email("boorwonie@email.com")
                .name("브라운")
                .password("subway")
                .build();

        return findUser;
    }

    public void deleteById(Long id) {
        // delete user
    }
}
