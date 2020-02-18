package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

import static atdd.path.UserConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
public class UserDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private UserDao userDao;

    @BeforeEach
    void setUp(){
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

    @Test
    public void saveUser(){
        User testUser = User.builder()
                .name(USER_NAME)
                .password(USER_PASSWORD)
                .build();
        User persistUser = userDao.save(testUser);
        assertThat(persistUser.getId()).isNotNull();
        assertThat(persistUser.getName()).isEqualTo(USER_NAME);
        assertThat(persistUser.getPassword()).isEqualTo(USER_PASSWORD);
    }

    @Test
    public void deleteByUserId(){
        User persisUser = userDao.save(TEST_USER);
        userDao.deleteByUserId(persisUser.getId());

        assertThrows(
                EmptyResultDataAccessException.class,
                ()-> userDao.findById(persisUser.getId())
        );
    }
}
