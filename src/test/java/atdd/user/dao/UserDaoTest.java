package atdd.user.dao;

import atdd.user.domain.Email;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class UserDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private UserDao userDao;

    @BeforeEach
    void setup() {
        this.userDao = new UserDao(jdbcTemplate);
    }

    @Test
    void create() throws Exception {
        final String emailAddress = "email@email.com";
        final String name = "name!!";
        final String password = "password!!";
        User user = User.create(new Email(emailAddress), name, password);

        final User createdUser = userDao.create(user);

        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getName()).isEqualTo(name);
        assertThat(createdUser.getEmail()).isEqualTo(emailAddress);
        assertThat(createdUser.getPassword()).isEqualTo(password);
    }

}