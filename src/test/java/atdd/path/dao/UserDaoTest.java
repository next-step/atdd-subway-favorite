package atdd.path.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static atdd.path.fixture.UserFixture.KIM_NAME;
import static atdd.path.fixture.UserFixture.KIM_USER;

@JdbcTest
public class UserDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    private UerDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao(jdbcTemplate);
        userDao.setDataSource(dataSource);
    }

    @Test
    public void save() {
        User persistUser = userDao.save(KIM_USER);

        assertThat(persistUser.getId()).isNotNull();
        assertThat(persistUser.getName()).isEqualTo(KIM_NAME);
    }
}
