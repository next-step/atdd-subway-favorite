package atdd.user.dao;

import atdd.user.domain.Email;
import atdd.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class UserDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private UserDao userDao;

    private final String emailAddress = "email@email.com";
    private final String name = "name!!";
    private final String password = "password!!";

    @BeforeEach
    void setup() {
        this.userDao = new UserDao(jdbcTemplate);
    }

    @Test
    void create() throws Exception {
        final User user = User.create(new Email(emailAddress), name, password);
        final User createdUser = userDao.create(user);

        assertThat(createdUser.getId()).isEqualTo(1L);
        assertThat(createdUser.getName()).isEqualTo(name);
        assertThat(createdUser.getEmail()).isEqualTo(emailAddress);
        assertThat(createdUser.getPassword()).isEqualTo(password);


        final User foundUser = userDao.findById(createdUser.getId());
        assertThat(foundUser).isEqualTo(createdUser);
    }

    @Test
    void delete() throws Exception {
        final User user = createUser();

        userDao.delete(user.getId());

        assertThatThrownBy(() -> userDao.findById(user.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private User createUser() {
        return userDao.create(User.create(new Email(emailAddress), name, password));
    }

}